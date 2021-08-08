package une.revilla.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
public class MessageResponse {

    private String message;

    // TODO: add a method that specifies the message

}
