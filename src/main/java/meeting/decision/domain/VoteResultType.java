package meeting.decision.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteResultType {
    YES("YES"), NO("NO"), ABSTAIN("ABSTAIN");
    private final String desc;
}
