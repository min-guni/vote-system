package meeting.decision.dto.vote;

import lombok.Data;

@Data
public class VoteUpdateDTO {
    private String voteName;
    private boolean isActivated;
}
