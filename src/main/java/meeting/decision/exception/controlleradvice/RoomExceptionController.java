package meeting.decision.exception.controlleradvice;

import meeting.decision.controller.RoomController;
import meeting.decision.exception.exceptions.UserNotInRoomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = RoomController.class)
public class RoomExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotInRoomException.class)
    public String UserNotInRoomBadRequestExHandle(UserNotInRoomException e){
        return e.getMessage();
    }
}
