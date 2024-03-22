package meeting.decision.exception;

public class UserNotInVoteRoomException extends RuntimeException{
    public UserNotInVoteRoomException(String username) {
        super(username + " USER IS NOT IN THE ROOM ");
    }
}
