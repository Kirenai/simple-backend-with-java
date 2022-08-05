package une.revilla.backend.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto {

    private Long id;

    @NotEmpty(message = "Please provide title of the task")
    @Size(max = 30, min = 3)
    private String title;

    @NotEmpty(message = "Please provide author of the task")
    @Size(max = 25, min = 3)
    private String author;

    @NotEmpty(message = "Please provide description of the task")
    @Size(max = 255)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String message;


}
