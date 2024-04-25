package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class VoteInDTO {
    private String voteName;
    private boolean isAnonymous;
}
