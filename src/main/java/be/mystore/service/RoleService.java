package be.mystore.service;

import be.mystore.model.ERole;
import be.mystore.model.Roles;

import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(ERole roleName);

    boolean existsByName(ERole roleName);

    void save(Roles role);
}
