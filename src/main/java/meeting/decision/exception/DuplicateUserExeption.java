package meeting.decision.exception;

public class DuplicateUserExeption extends RuntimeException{
    public DuplicateUserExeption() {
        super("USER IS ALREADY IN THE ROOM");
    }
}
