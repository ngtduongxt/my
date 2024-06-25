package be.mystore.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 8, message = "Username must be at least 8 characters long")
    @Size(max = 16, message = "Username must not exceed 16 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Size(max = 16, message = "Password must not exceed 16 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespaces.")
    private String password;

    @NotBlank
    private String fullName;

    @NotBlank(message = "Mail is required")
    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String address;

    private Set<String> listRoles;
}
