package be.mystore.service;

import be.mystore.model.User;
import be.mystore.model.dto.UserInfoDTO;

import java.util.Optional;


public interface UserService {

    Optional<User> findByUsername(String username);

    boolean existByEmail(String email);

    boolean existByUsername(String username);

    void create(User users);

    Optional<User> findByActivationCode(String tokenAccount);

    void changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword);

    void resetPassword(String email);

    UserInfoDTO getUserInfo(Long userId);

    void updateProfile(Long userId, UserInfoDTO userInfoDTO);

}
