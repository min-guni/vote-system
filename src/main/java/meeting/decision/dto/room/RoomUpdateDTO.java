package meeting.decision.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import meeting.decision.domain.User;

@AllArgsConstructor
@Getter
public class RoomUpdateDTO {
    private Long ownerId;
    private String roomName;
}
