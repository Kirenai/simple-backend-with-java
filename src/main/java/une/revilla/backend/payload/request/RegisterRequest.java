package une.revilla.backend.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Please provide username")
    @Size(min = 2, max = 30)
    private String username;

    @NotBlank(message = "Please provide password")
    @Size(min = 2, max = 40)
    private String password;

    @NotBlank(message = "Please provide email")
    @Size(max = 35)
    @Email(message = "Please insert a good email")
    private String email;

    @NotBlank(message = "Please provide full name")
    @Size(min = 10, max = 40)
    private String fullName;

    private Set<String> roles;

}
