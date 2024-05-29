package meeting.decision.exception.exceptions;

public class AlreadyVotedException extends RuntimeException {
    public AlreadyVotedException() {
    }

    public AlreadyVotedException(String message) {
        super(message);
    }
}
