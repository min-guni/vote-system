package meeting.decision.dto;

import lombok.Data;

@Data
public class VoteUpdateDTO {
    private String voteName;
    private boolean isActivated;
}
