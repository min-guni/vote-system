package meeting.decision.exception.exceptions;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException() {
        super("ROOM IS NOT FOUND");
    }
}
