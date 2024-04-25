package meeting.decision.repository;

import meeting.decision.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface JpaVoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByRoomId(Long id);

}
