package meeting.decision.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class VoteResultDTO {
    private Long yesNum;
    private List<String> yesList = new ArrayList<>();
    private Long noNum;
    private List<String> noList = new ArrayList<>();

    public VoteResultDTO(Long yesNum, Long noNum) {
        this.yesNum = yesNum;
        this.noNum = noNum;
    }
}
