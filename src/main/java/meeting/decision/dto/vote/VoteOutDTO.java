package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteOutDTO {
    private Long id;
    private Long roomId;
    private String voteName;
    private boolean isActivated;
    private boolean isAnonymous;

    private VoteResultDTO voteResult;
}
