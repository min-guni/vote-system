package meeting.decision.exception.exceptions;

public class VoteNotFoundException extends RuntimeException{
    public VoteNotFoundException() {
        super("VOTE NOT FOUND");
    }
}
