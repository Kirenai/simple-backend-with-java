package une.revilla.backend.exception.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthErrorMessage {

    private Integer statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;

}
