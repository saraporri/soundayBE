package it.epicode.sounday.user;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findOneByUsername(String username);

    Optional<User> findByEmail(String email);
    List<User> findByUsernameContainingAndRoles_roleType(String username, String roleType);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
