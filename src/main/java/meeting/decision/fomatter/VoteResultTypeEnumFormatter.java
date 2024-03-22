package meeting.decision.fomatter;

import meeting.decision.domain.VoteResultType;
import meeting.decision.exception.VoteResultTypeEnumParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class VoteResultTypeEnumFormatter implements Formatter<VoteResultType> {
    @Override
    public VoteResultType parse(String text, Locale locale) throws ParseException {
        if(text.equals("YES")){
            return VoteResultType.YES;
        }
        else if(text.equals("NO")){
            return VoteResultType.NO;
        }
        else if(text.equals("ABSTAIN")){
            return VoteResultType.ABSTAIN;
        }
        throw new VoteResultTypeEnumParseException();
    }

    @Override
    public String print(VoteResultType object, Locale locale) {
        return object.getDesc();
    }
}
