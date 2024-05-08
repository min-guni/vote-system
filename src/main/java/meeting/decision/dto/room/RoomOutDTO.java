package meeting.decision.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class RoomOutDTO {
    private Long roomId;
    private String roomName;
    private Long ownerId;
    private Long userNum = 0L;
    private LocalDateTime createTime;
}