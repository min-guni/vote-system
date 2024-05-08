package meeting.decision.exception.exceptions;

public class UserNotInRoomException extends RuntimeException{
    public UserNotInRoomException(Long userId) {
        super(userId + " USER IS NOT IN THE ROOM ");
    }
}
