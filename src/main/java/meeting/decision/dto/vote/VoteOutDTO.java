package meeting.decision.dto.vote;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VoteOutDTO {
    private Long id;
    private Long roomId;
    private String voteName;
    private boolean activated;
    private boolean anonymous;
    private Long yesNum;
    private Long noNum;
    private Long abstentionNum;
    private Optional<List<VoteResultDTO>> voteResult;
    private LocalDateTime startTime;

    public VoteOutDTO(Long id, Long roomId, String voteName, boolean activated, boolean anonymous, Long yesNum, Long noNum, Long abstentionNum, LocalDateTime startTime) {
        this.id = id;
        this.roomId = roomId;
        this.voteName = voteName;
        this.activated = activated;
        this.anonymous = anonymous;
        this.yesNum = yesNum;
        this.noNum = noNum;
        this.abstentionNum = abstentionNum;
        voteResult = Optional.empty();
        this.startTime = startTime;
    }

    public void setVoteResult(Optional<List<VoteResultDTO>> voteResult) {
        this.voteResult = voteResult;
    }
}
