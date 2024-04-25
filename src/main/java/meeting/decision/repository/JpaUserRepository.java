package meeting.decision.repository;

import meeting.decision.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface JpaUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}