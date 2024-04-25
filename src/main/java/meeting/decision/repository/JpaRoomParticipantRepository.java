package meeting.decision.repository;

import meeting.decision.domain.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface JpaRoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {

    //CRITERIA 자동으로 하면 JOIN 을 실시함 JOIN을 실시하지 않게 JPQL로 바꾸자
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM RoomParticipant rp WHERE rp.room.id = :roomId and rp.user.id = :userId) THEN true ELSE false END")
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);

    @Query("SELECT rp FROM RoomParticipant rp WHERE rp.room.id = :roomId and rp.user.id = :userId")
    Optional<RoomParticipant> findByRoomIdAndUserId(Long roomId, Long userId);
}
