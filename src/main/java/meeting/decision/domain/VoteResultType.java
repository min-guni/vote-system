package meeting.decision.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum VoteResultType {
    YES("YES"), NO("NO"), ABSTAIN("ABSTAIN");
    private final String desc;
}
