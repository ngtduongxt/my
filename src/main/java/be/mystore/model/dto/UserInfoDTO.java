package be.mystore.model.dto;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long userId;
    private String fullName;
    private String avatar;
    private String email;
    private String phone;
    private String address;
}
