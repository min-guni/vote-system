package meeting.decision.exception.controlleradvice;


import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import meeting.decision.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameAlreadyExistsException.class,
            NoSuchElementException.class,
            DuplicateRequestException.class,
            VoteResultTypeEnumParseException.class,
            DuplicateUserExeption.class,
            OwnerDeleteException.class})
    public String BadRequestExHandle(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({LoginFailedException.class, UserNotFoundErrorException.class, TokenAuthenticationException.class})
    public String loginFailExHandle(RuntimeException e){
        return e.getMessage();
    }



    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({RoomNotFoundException.class,VoteNotFoundException.class})
    public String notFoundExHandle(RuntimeException e) {return e.getMessage();}

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({VoteIsNotActivatedException.class,AuthorizationServiceException.class})
    public String forbiddenHandle(RuntimeException e){
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


}
