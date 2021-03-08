package une.revilla.backend.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    @NotBlank(message = "required id")
    private Long id;

    @NotBlank(message = "required title")
    private String title;

    @NotBlank(message = "required author")
    private String author;

    @NotBlank(message = "required description")
    private String description;

}
