package meeting.decision.repository;

import meeting.decision.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface JpaVoteRepository extends JpaRepository<Vote, Long> {
    Set<Vote> findByRoomId(Long id);
}
