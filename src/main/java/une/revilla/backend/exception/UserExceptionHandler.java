package une.revilla.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import une.revilla.backend.exception.user.UserErrorMessage;
import une.revilla.backend.exception.user.UserNoSuchElementException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(UserNoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public UserErrorMessage noSuchElementException(UserNoSuchElementException ex, WebRequest request) {
        logger.error("Error User/Service : {}", ex.getMessage());
        return new UserErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

}
