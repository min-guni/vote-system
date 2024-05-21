package meeting.decision.repository;

import meeting.decision.domain.Vote;
import meeting.decision.dto.vote.VoteOutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaVoteRepository extends JpaRepository<Vote, Long> {


    @Query("SELECT v FROM Vote v LEFT JOIN FETCH v.papers vp LEFT JOIN FETCH vp.user Where v.id = :voteId")
    Optional<Vote> findVoteByVoteIdWithFetch(Long voteId);


    @Query("FROM Vote v JOIN FETCH v.papers WHERE v.room.id = :roomId")
    List<Vote> findVoteByRoomId(Long roomId);



    @Modifying
    @Query("DELETE FROM Vote v WHERE v.room.id = :roomId")
    void deleteVoteByRoomId(Long roomId);
}
