package meeting.decision.repository;

import meeting.decision.domain.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface JpaRoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {

    //CRITERIA 자동으로 하면 JOIN 을 실시함 JOIN을 실시하지 않게 JPQL로 바꾸자
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM RoomParticipant rp WHERE rp.room.id = :roomId and rp.user.id = :userId) THEN true ELSE false END")
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    @Query("SELECT rp FROM RoomParticipant rp WHERE rp.room.id = :roomId and rp.user.id = :userId")
    Optional<RoomParticipant> findByRoomIdAndUserId(Long roomId, Long userId);

    @Modifying //jpql은 기본적으로 조회를 위한 것이기 때문에 delete나 update를 할 때에는 @modifying을 달아줘야함
    @Query("DELETE FROM RoomParticipant rp WHERE rp.room.id = :roomId and rp.user.id = :userId")
    void deleteByRoomIdAndUserId(Long roomId, Long userId);
}
