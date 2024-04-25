package meeting.decision.repository;

import meeting.decision.domain.VotePaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface JpaVotePaperRepository extends JpaRepository<VotePaper, Long> {
      boolean existsByVoteIdAndUserId(Long voteId, Long userId);
}
