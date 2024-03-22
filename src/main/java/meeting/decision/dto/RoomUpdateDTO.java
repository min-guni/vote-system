package meeting.decision.dto;

import lombok.Data;
import meeting.decision.domain.User;

@Data
public class RoomUpdateDTO {
    private User owner;
    private String roomName;
}
