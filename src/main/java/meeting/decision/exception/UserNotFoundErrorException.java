package meeting.decision.exception;

public class UserNotFoundErrorException extends RuntimeException{
    public UserNotFoundErrorException() {
        super("USER NOT FOUND");
    }
}
