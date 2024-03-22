package meeting.decision.exception;

public class VoteResultTypeEnumParseException extends RuntimeException{
    public VoteResultTypeEnumParseException() {
        super("only YES OR NO OR ABSTAIN CAN BE REQUESTED");
    }

}
