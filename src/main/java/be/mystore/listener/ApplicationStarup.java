package be.mystore.listener;

import be.mystore.model.ERole;
import be.mystore.model.Roles;
import be.mystore.model.User;
import be.mystore.service.impl.RoleServiceImpl;
import be.mystore.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ApplicationStarup {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Create roles
        if (!roleService.existsByName(ERole.ROLE_ADMIN)) {
            roleService.save(new Roles(ERole.ROLE_ADMIN));
        }
        if (!roleService.existsByName(ERole.ROLE_STAFF)) {
            roleService.save(new Roles(ERole.ROLE_STAFF));
        }
        if (!roleService.existsByName(ERole.ROLE_USER)) {
            roleService.save(new Roles(ERole.ROLE_USER));
        }

        // Create default admin account
        if (!userService.existByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@mystore.com");
            admin.setActivated(true);
            admin.setPassword(passwordEncoder.encode("admin@1234"));

            Set<Roles> roles = new HashSet<>();
            Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            admin.setRoles(roles);
            userService.create(admin);
        }
    }
}
