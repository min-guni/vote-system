package meeting.decision.repository;

import meeting.decision.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaRoomRepository extends JpaRepository<Room, Long> {


}
