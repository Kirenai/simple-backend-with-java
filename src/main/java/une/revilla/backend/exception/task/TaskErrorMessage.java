package une.revilla.backend.exception.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskErrorMessage {

    private Integer statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;

}
