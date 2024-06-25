package be.mystore.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "Roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ERole roleName;


    public Roles(ERole roleName) {
        this.roleName = roleName;
    }

    public Roles() {
    }
}
