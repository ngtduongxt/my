package be.mystore.model.dto;

import be.mystore.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDTO {
    private long userId;
    private String username;
    private String avatar;
    private String email;
    private String phone;
    private String token;
    private Roles roles;
}
