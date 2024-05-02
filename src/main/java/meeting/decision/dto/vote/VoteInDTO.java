package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @AllArgsConstructor @ToString
public class VoteInDTO {
    private String voteName;
    private boolean anonymous;
}
