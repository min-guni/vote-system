package meeting.decision.repository;

import meeting.decision.domain.Room;
import meeting.decision.dto.room.RoomOutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



public interface JpaRoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT new meeting.decision.dto.room.RoomOutDTO(rp.room.id, rp.room.roomName, rp.room.owner.id, COUNT(rp))FROM RoomParticipant rp JOIN rp.room GROUP BY rp.room.id")
    List<RoomOutDTO> findAllDTO();

    @Query("SELECT new meeting.decision.dto.room.RoomOutDTO(rp.room.id, rp.room.roomName, rp.room.owner.id, COUNT(rp))" +
            " FROM RoomParticipant rp JOIN rp.room GROUP BY rp.room.id " +
            "HAVING rp.room.id IN (SELECT rp.room.id FROM RoomParticipant rp WHERE rp.user.id = :userId)")
    List<RoomOutDTO> findByIdDTO(@Param("userId") Long userId);
}
