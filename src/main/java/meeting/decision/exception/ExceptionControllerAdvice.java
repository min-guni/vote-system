package meeting.decision.exception;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String userNameAlreadyExHandle(UsernameAlreadyExistsException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({LoginFailedException.class, UserNotFoundErrorException.class})
    public String loginFailExHandle(RuntimeException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationServiceException.class)
    public String forbiddenHandle(AuthorizationServiceException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VoteResultTypeEnumParseException.class)
    public String voteResultTypeEnumParseExHandle(VoteResultTypeEnumParseException e){
        return e.getMessage();
    }

}
