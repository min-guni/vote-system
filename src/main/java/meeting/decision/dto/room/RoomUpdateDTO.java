package meeting.decision.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RoomUpdateDTO {
    private Long ownerId;
    private String roomName;
}
