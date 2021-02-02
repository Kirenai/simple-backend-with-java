package une.revilla.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import une.revilla.backend.exception.task.TaskErrorMessage;
import une.revilla.backend.exception.task.TaskNoSuchElementException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TaskExceptionHandler {

    @ExceptionHandler(TaskNoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public TaskErrorMessage noSuchElementException(TaskNoSuchElementException ex, WebRequest request) {
        return new TaskErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public TaskErrorMessage UserTaskNoSuchElementException(WebRequest request) {
//        return null;
//    }
}
