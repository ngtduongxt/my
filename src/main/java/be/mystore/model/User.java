package be.mystore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(unique = true)
    @Size(max = 50)
    private String username;
    @Column(nullable = false)
    @Size(max = 255)
    private String password;
    private String fullName;

    private String avatar;
    @Column(unique = true)
    @Size(max = 255)
    private String email;
    @Column(unique = true)
    @Size(max = 13)
    private String phone;
    @Size(max = 255)
    private String address;
    @ManyToMany
    @JoinTable(name = "User_Role", joinColumns = @JoinColumn(name = "UserId"), inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private Set<Roles> listRoles = new HashSet<>();

    private String activationCode;
    private LocalDateTime expiredTime;


    @Column(nullable = false)
    private boolean activated;

    @OneToMany
    private List<Review> reviews;


    public void setRoles(Set<Roles> roles) {
        this.listRoles = roles;
    }

    public String getUsername() {
        return username.toLowerCase();
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }
}