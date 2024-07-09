package it.epicode.sounday.user;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String UserName);
    boolean existsByEmail(String Email);
}
