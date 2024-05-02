package meeting.decision.repository;

import com.querydsl.core.annotations.QueryInit;
import meeting.decision.domain.VotePaper;
import meeting.decision.dto.vote.VoteResultDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface JpaVotePaperRepository extends JpaRepository<VotePaper, Long> {
      //얘도 criteria 자동 생성시 join 이 됨 jpql로 바꿔주자
      @Query("SELECT CASE WHEN EXISTS (SELECT 1 from VotePaper vp WHERE vp.vote.id = :voteId and vp.user.id = :userId) THEN true ELSE false END")
      boolean existsByVoteIdAndUserId(Long voteId, Long userId);

      @Query("SELECT v.voteResultType, COUNT(v) FROM VotePaper v WHERE v.vote.id = :voteId GROUP BY v.voteResultType")
      List<Object[]> countVoteResultByType(Long voteId);


      @Query("SELECT new meeting.decision.dto.vote.VoteResultDTO(v.user.id, v.user.username, v.voteResultType) FROM VotePaper v JOIN v.user WHERE v.vote.id = :voteId")
      List<VoteResultDTO> getVoteResultDTOByVoteID(Long voteId);


      @Modifying
      @Query("DELETE FROM VotePaper vp WHERE vp.vote = :voteId")
      void deleteVotePaperByVoteId(Long voteId);

      @Modifying
      @Query("DELETE FROM VotePaper vp WHERE vp.vote.room.id = :roomId")
      void deleteVotePaperByRoomId(Long roomId);

}
