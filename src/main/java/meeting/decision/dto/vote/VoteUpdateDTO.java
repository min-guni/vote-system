package meeting.decision.dto.vote;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class VoteUpdateDTO {
    private String voteName;
    private boolean activated;
}
