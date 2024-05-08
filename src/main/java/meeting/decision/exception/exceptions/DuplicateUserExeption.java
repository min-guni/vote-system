package meeting.decision.exception.exceptions;

public class DuplicateUserExeption extends RuntimeException{
    public DuplicateUserExeption() {
        super("USER IS ALREADY IN THE ROOM");
    }
}
