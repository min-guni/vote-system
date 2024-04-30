package meeting.decision.repository;

import meeting.decision.domain.Room;
import meeting.decision.dto.room.RoomOutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



public interface JpaRoomRepository extends JpaRepository<Room, Long> {


}
