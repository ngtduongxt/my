package be.mystore.repository;

import be.mystore.model.ERole;
import be.mystore.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(ERole roleName);

    boolean existsByRoleName(ERole roleName);
}
