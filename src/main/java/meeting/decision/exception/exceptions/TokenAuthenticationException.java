package meeting.decision.exception.exceptions;

public class TokenAuthenticationException extends RuntimeException{
    public TokenAuthenticationException() {
        super("Login Failed");
    }



    public TokenAuthenticationException(Throwable cause) {
        super("Login Failed" , cause);
    }
}
