package meeting.decision.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class RoomOutDTO {
    private Long roomId;
    private String roomName;
    private Long ownerId;
    private Long userNum = 0L;
    private LocalDateTime createTime;
}