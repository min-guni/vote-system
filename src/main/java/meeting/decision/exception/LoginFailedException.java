package meeting.decision.exception;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException(){
        super("Login Failed");
    }
}
