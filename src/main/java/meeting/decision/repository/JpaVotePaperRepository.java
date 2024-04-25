package meeting.decision.repository;

import com.querydsl.core.annotations.QueryInit;
import meeting.decision.domain.VotePaper;
import meeting.decision.dto.vote.VoteResultDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface JpaVotePaperRepository extends JpaRepository<VotePaper, Long> {
      //얘도 criteria 자동 생성시 join 이 됨 jpql로 바꿔주자
      @Query("SELECT vp.id from VotePaper vp WHERE vp.vote.id = :voteId and vp.user.id = :userId")
      boolean existsByVoteIdAndUserId(Long voteId, Long userId);

      @Query("SELECT v.voteResultType, COUNT(v) FROM VotePaper v WHERE v.vote.id = :voteId GROUP BY v.voteResultType")
      List<Object[]> countVoteResultByType(Long voteId);


      @Query("SELECT new meeting.decision.dto.vote.VoteResultDTO(v.user.id, v.user.username, v.voteResultType) FROM VotePaper v WHERE v.vote.id = :voteId")
      List<VoteResultDTO> getVoteResultDTOByVoteID(Long voteId);

}
