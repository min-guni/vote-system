package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import meeting.decision.domain.VoteResultType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class VoteResultDTO {
    private Long userId;
    private String username;
    private VoteResultType voteResultType;
}
