package be.mystore.controller;

import be.mystore.model.ERole;
import be.mystore.model.Roles;
import be.mystore.model.User;
import be.mystore.payload.request.LoginRequest;
import be.mystore.payload.request.RegisterRequest;
import be.mystore.payload.response.JwtResponse;
import be.mystore.security.jwt.JwtUtils;
import be.mystore.security.service.UserDetailsImpl;
import be.mystore.service.ImageService;
import be.mystore.service.impl.RoleServiceImpl;
import be.mystore.service.impl.UserServiceImpl;
import be.mystore.util.EmailContentBuilder;
import be.mystore.util.EmailSender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ImageService imageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RoleServiceImpl roleService;
    @Autowired
    private EmailSender emailSender;


    //User đăng nhập.
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            //Check if the account is activated
            if (!userDetails.isActivated()) {
                //If not activative, generate a new token
                String newCode = UUID.randomUUID().toString();
                //Savethe new token to the database
                User user = userService.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
                user.setActivationCode(newCode);
                user.setExpiredTime(LocalDateTime.now());
                userService.create(user);

                //Send the activation email
                String activationLink = "http://localhost:8080/api/auth/activate?code=" + newCode;
                String emailContent = EmailContentBuilder.buildActivationEmail(activationLink);
                emailSender.sendMail(user.getEmail(), "Kích hoạt tài khoản", emailContent);
                return ResponseEntity.ok("Tài khoản của bạn chưa được kích hoạt. Vui lòng kiểm tra email của bạn để kích hoạt tài khoản.");
            }

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    userDetails.getFullName(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    userDetails.getPhone(),
                    userDetails.isActivated(),
                    roles));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tài khoản hoặc mật khẩu không chính xác");
        }
    }

    //Đăng ký user
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        //kiểm tra username tồn tại hay chưa
        if (userService.existByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Tên người dùng đã tồn tại");
        }

        //kiểm tra email user đã tồn tại chưa
        if (userService.existByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã tồn tại");
        }


        //Tạo User từ thông tin đăng ký
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        String code = UUID.randomUUID().toString();
        user.setActivationCode(code);
        user.setExpiredTime(LocalDateTime.now());
        Set<String> strRole = registerRequest.getListRoles();
        user.setAddress(registerRequest.getAddress());
        Set<Roles> listRoles = new HashSet<>();
        if (strRole == null || strRole.isEmpty()) {
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
            listRoles.add(userRole);
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
                        listRoles.add(adminRole);
                        break;
                    case "staff":
                        Roles staffRole = roleService.findByRoleName(ERole.ROLE_STAFF).orElseThrow(() -> new RuntimeException("Vai trò không tồn tại"));
                        listRoles.add(staffRole);
                        break;
                }
            });
        }
        user.setListRoles(listRoles);

        String imageUrl = imageService.getImageUrl();
        user.setAvatar(imageUrl);
        userService.create(user);

        String activationLink = "http://localhost:8080/api/auth/activate?code=" + code;
        String emailContent = EmailContentBuilder.buildActivationEmail(activationLink);
        emailSender.sendMail(user.getEmail(), "Kích hoạt tài khoản", emailContent);
        return ResponseEntity.ok("Đăng ký tài khoản thành công! Vui lòng kiểm tra email của bạn để kích hoạt tài khoản.");

    }

    //Kích hoạt tài khoản
    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("code") String code) {
        //Tìm kiếm người dùng dựa trên token kích hoạt.
        Optional<User> user = userService.findByActivationCode(code);

        //Kiểm tra token có hợp lệ hay không.
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Liên kết kích hoạt không hợp lệ");
        }

        //Kiểm tra xem tài khoản đã được kích hoạt chưa.
        if (user.get().isActivated()) {
            return ResponseEntity.badRequest().body("Tài khoản đã được kích hoạt");
        }


        //Kích hoạt tài khoản
        user.get().setActivated(true);
        user.get().setActivationCode(null);
        user.get().setExpiredTime(null);
        userService.create(user.orElse(null));

        return ResponseEntity.ok("Tài khoản được kích hoạt thành công");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        try {
            userService.resetPassword(email);
            return ResponseEntity.ok("Đặt lại mật khẩu thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Đã đăng xuất");
    }
}
