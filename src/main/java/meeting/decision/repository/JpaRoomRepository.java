package meeting.decision.repository;

import meeting.decision.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface JpaRoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r JOIN FETCH r.userList rp WHERE rp.user.id = :userId")
    List<Room> findRoomByUserId(Long userId);

}
