package meeting.decision.exception.exceptions;

public class UserNotFoundErrorException extends RuntimeException{
    public UserNotFoundErrorException() {
        super("USER NOT FOUND");
    }
}
