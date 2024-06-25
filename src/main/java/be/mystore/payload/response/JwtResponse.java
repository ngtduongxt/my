package be.mystore.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private boolean activated;
    private List<String> role;

    public JwtResponse(String token, String fullName, String userName, String email, String phone, boolean activated, List<String> role) {
        this.token = token;
        this.fullName = fullName;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.activated = activated;
        this.role = role;
    }
}
