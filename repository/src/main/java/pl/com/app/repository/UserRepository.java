package pl.com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.app.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByLockedIsTrue();
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);

}
