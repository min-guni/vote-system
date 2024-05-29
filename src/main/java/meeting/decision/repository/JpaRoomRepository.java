package meeting.decision.repository;

import meeting.decision.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface JpaRoomRepository extends JpaRepository<Room, Long> {

}
