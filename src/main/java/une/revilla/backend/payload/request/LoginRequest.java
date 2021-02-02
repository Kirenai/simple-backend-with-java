package une.revilla.backend.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {

    @NotBlank(message = "Please provide Username")
    private String username;
    @NotBlank(message = "Please provide Password")
    private String password;

}
