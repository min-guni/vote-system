package meeting.decision.exception.exceptions;

public class VoteResultTypeEnumParseException extends RuntimeException{
    public VoteResultTypeEnumParseException() {
        super("only YES OR NO OR ABSTAIN CAN BE REQUESTED");
    }

}
