package be.mystore.repository;

import be.mystore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByActivationCode(String activationCode);

    Optional<User> findByEmail(String email);
    @Modifying
    @Query("UPDATE User u SET u.activationCode = NULL, u.expiredTime = NULL WHERE u.expiredTime < :time")
    void deleteTokensCreatedBefore(@Param("time")LocalDateTime time);
}
