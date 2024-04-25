package meeting.decision.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.exception.EnumBadRequestException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum VoteResultType {
    YES("YES"), NO("NO"), ABSTAIN("ABSTAIN");
    private final String desc;
}
