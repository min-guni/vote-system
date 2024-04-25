package meeting.decision.dto.room;

import lombok.Data;
import meeting.decision.domain.User;

@Data
public class RoomUpdateDTO {
    private User owner;
    private String roomName;
}
