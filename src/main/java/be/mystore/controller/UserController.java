package be.mystore.controller;

import be.mystore.model.dto.UserInfoDTO;
import be.mystore.security.service.UserDetailsImpl;
import be.mystore.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@Controller
@RequestMapping("/user")
@Validated
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;


    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirmPassword) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            userService.changePassword(userDetails.getId(), oldPassword, newPassword, confirmPassword);

            return ResponseEntity.ok("Đã thay đổi mật khẩu thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInfoDTO> profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserInfoDTO userInfoDTO = userService.getUserInfo(userDetails.getId());
        return ResponseEntity.ok(userInfoDTO);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserInfoDTO> updateProfile(@RequestBody UserInfoDTO userInfoDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        userService.updateProfile(userDetails.getId(), userInfoDTO);
        return ResponseEntity.ok(userInfoDTO);
    }
}