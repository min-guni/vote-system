package meeting.decision.exception.exceptions;

public class LoginFailedException extends RuntimeException{
    public LoginFailedException(){
        super("Login Failed");
    }
}
