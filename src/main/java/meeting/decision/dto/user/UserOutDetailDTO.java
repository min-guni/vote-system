package meeting.decision.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserOutDetailDTO {
    private Long id;
    private String username;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}