package meeting.decision.dto.vote;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter @AllArgsConstructor @ToString
public class VoteInDTO {

    @NotBlank
    private String voteName;

    private boolean anonymous;
}
