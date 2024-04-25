package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class VoteOutDTO {
    private Long id;
    private Long roomId;
    private String voteName;
    private boolean isActivated;
    private boolean isAnonymous;
    private Long yesNum;
    private Long noNum;
    private Long abstentionNum;
    private List<VoteResultDTO> voteResult;

    public VoteOutDTO(Long id, Long roomId, String voteName, boolean isActivated, boolean isAnonymous, Long yesNum, Long noNum, Long abstentionNum) {
        this.id = id;
        this.roomId = roomId;
        this.voteName = voteName;
        this.isActivated = isActivated;
        this.isAnonymous = isAnonymous;
        this.yesNum = yesNum;
        this.noNum = noNum;
        this.abstentionNum = abstentionNum;
        voteResult = null;
    }
}
