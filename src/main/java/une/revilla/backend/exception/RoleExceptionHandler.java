package une.revilla.backend.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import une.revilla.backend.exception.role.RoleErrorMessage;
import une.revilla.backend.exception.role.RoleNoSuchElementException;

@RestControllerAdvice
public class RoleExceptionHandler {

    @ExceptionHandler(RoleNoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RoleErrorMessage noSuchElementException(RoleNoSuchElementException ex, WebRequest request) {
        return new RoleErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage(),
                request.getDescription(false));
    }

}
