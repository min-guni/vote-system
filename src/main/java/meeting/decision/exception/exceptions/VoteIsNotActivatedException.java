package meeting.decision.exception.exceptions;

public class VoteIsNotActivatedException extends RuntimeException{
    public VoteIsNotActivatedException(String message) {
        super(message);
    }
}
