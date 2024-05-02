package meeting.decision.exception;

public class VoteNotFoundException extends RuntimeException{
    public VoteNotFoundException() {
        super("VOTE NOT FOUND");
    }
}
