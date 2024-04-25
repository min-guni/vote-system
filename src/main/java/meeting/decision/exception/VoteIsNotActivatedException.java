package meeting.decision.exception;

public class VoteIsNotActivatedException extends RuntimeException{
    public VoteIsNotActivatedException(String message) {
        super(message);
    }
}
