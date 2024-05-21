package meeting.decision.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum VoteResultType {
    YES("YES"), NO("NO"), ABSTAIN("ABSTAIN");
    private final String desc;
}
