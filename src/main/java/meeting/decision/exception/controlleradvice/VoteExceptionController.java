package meeting.decision.exception.controlleradvice;

import meeting.decision.controller.VoteController;
import meeting.decision.controller.VotePaperController;
import meeting.decision.exception.exceptions.UserNotInRoomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {VoteController.class, VotePaperController.class})
public class VoteExceptionController {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UserNotInRoomException.class)
    public String UserNotInRoomBadRequestExHandle(UserNotInRoomException e){
        return e.getMessage();
    }
}
