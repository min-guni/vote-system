package meeting.decision.repository;

import meeting.decision.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaRoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "SELECT COUNT(u.id) > 0 FROM room_participants rp JOIN users u ON rp.user_id = u.id WHERE rp.room_id = :roomId AND u.id = :userId", nativeQuery = true)
    Integer existsUserInRoom(@Param("roomId") Long roomId, @Param("userId") Long userId);
}
