package meeting.decision.dto.vote;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
public class VoteUpdateDTO {
    @NotBlank
    private String voteName;
    private boolean activated;
}
