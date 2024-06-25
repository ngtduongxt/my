package be.mystore.service.impl;

import be.mystore.model.ERole;
import be.mystore.model.Roles;
import be.mystore.repository.RoleRepository;
import be.mystore.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Roles> findByRoleName(ERole roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public boolean existsByName(ERole roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    @Override
    public void save(Roles role) {
        roleRepository.save(role);
    }


}
