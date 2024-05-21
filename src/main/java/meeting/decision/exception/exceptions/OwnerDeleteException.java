package meeting.decision.exception.exceptions;

public class OwnerDeleteException extends RuntimeException{
    public OwnerDeleteException() {
        super("Owner cannot be deleted from room");
    }
}
