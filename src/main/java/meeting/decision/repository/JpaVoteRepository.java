package meeting.decision.repository;

import meeting.decision.domain.Vote;
import meeting.decision.dto.vote.VoteOutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface JpaVoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByRoomId(Long id);


    @Query("SELECT new meeting.decision.dto.vote.VoteOutDTO(" +
            "v.id, v.room.id, v.voteName, v.isActivated, v.isAnonymous, " +
            "SUM(CASE WHEN vp.voteResultType = meeting.decision.domain.VoteResultType.YES THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN vp.voteResultType = meeting.decision.domain.VoteResultType.NO THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN vp.voteResultType = meeting.decision.domain.VoteResultType.ABSTAIN THEN 1 ELSE 0 END)) " +
            "FROM Vote v " +
            "LEFT JOIN v.papers vp " +
            "WHERE v.room.id = :roomId " +
            "GROUP BY v.id, v.room.id, v.voteName, v.isActivated, v.isAnonymous")//group by를 비잡계 모든 컬럼에 하는 것이 좋다고 한다..관습이랜다..
    List<VoteOutDTO> findVoteOutDTOByRoomId(@Param("roomId") Long roomId);
}
