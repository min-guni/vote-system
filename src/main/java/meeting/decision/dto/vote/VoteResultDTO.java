package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import meeting.decision.domain.VoteResultType;

@AllArgsConstructor
@Getter
@ToString
public class VoteResultDTO {
    private Long userId;
    private String username;
    private VoteResultType voteResultType;
}
