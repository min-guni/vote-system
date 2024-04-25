package meeting.decision.exception;


import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameAlreadyExistsException.class,
            NoSuchElementException.class,
            DuplicateRequestException.class,
            VoteIsNotActivatedException.class,
            VoteResultTypeEnumParseException.class,
            DuplicateUserExeption.class})
    public String BadRequestExHandle(Exception e) {
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



}
