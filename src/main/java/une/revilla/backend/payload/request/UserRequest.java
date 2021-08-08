package une.revilla.backend.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class UserRequest {

    @NotBlank(message = "required username")
    private String username;
    @NotBlank(message = "required password")
    private String password;
    @NotBlank(message = "required email")
    @Email(message = "Insert email correctly")
    private String email;
    @NotBlank(message = "required full name")
    private String fullName;
    @NotBlank(message = "A minimum role required")
    @NotNull(message = "A role is required")
    private Set<String> roles;

}
