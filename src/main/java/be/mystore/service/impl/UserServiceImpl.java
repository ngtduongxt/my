package be.mystore.service.impl;

import be.mystore.model.User;
import be.mystore.model.dto.UserInfoDTO;
import be.mystore.repository.UserRepository;
import be.mystore.service.UserService;
import be.mystore.util.EmailContentBuilder;
import be.mystore.util.EmailSender;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSender emailSender;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void create(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByActivationCode(String codeActivate) {
        return userRepository.findByActivationCode(codeActivate);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            if (passwordEncoder.matches(oldPassword, user.get().getPassword())) {
                if (newPassword.equals(confirmPassword)) {
                    user.get().setPassword(passwordEncoder.encode(newPassword));
                    create(user.get());
                } else {
                    throw new RuntimeException("Mật khẩu mới không khớp");
                }
            } else {
                throw new RuntimeException("Mật khẩu cũ không đúng");
            }
        } else {
            throw new RuntimeException("Người dùng không tồn tại");
        }
    }

    @Override
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
        //Generate new random password
        String newPassword = generateRandomPassword();

        String userName = user.getUsername();

        //Encode and set the new password
        user.setPassword(passwordEncoder.encode(newPassword));
        create(user);

        String mailContent = EmailContentBuilder.buildPasswordResetEmail(userName, newPassword);
        emailSender.sendMail(email, "Reset Password", mailContent);

    }

    @Override
    public UserInfoDTO getUserInfo(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setUserId(user.get().getUserId());
            userInfoDTO.setFullName(user.get().getFullName());
            userInfoDTO.setAvatar(user.get().getAvatar());
            userInfoDTO.setEmail(user.get().getEmail());
            userInfoDTO.setPhone(user.get().getPhone());
            userInfoDTO.setAddress(user.get().getAddress());
            return userInfoDTO;
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    @Override
    public void updateProfile(Long userId, UserInfoDTO userInfoDTO) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setFullName(userInfoDTO.getFullName());
            user.get().setAvatar(userInfoDTO.getAvatar());
            user.get().setEmail(userInfoDTO.getEmail());
            user.get().setPhone(userInfoDTO.getPhone());
            user.get().setAddress(userInfoDTO.getAddress());
            create(user.get());
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        //Generate a random password with length of 8
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }



    @Transactional
    public void removeExpTokens() {
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(24);
        userRepository.deleteTokensCreatedBefore(expirationTime);
    }

}
