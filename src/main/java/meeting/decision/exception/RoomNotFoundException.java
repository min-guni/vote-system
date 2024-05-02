package meeting.decision.exception;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException() {
        super("ROOM IS NOT FOUND");
    }
}
