package meeting.decision.repository;

import meeting.decision.domain.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaRoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    boolean existsByRoomIdAndUserId(Long roomId, Long userId);
    Optional<RoomParticipant> findByRoomIdAndUserId(Long roomId, Long userId);
}
