package une.revilla.backend.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true) //Setters return this | object.set().set().set().etc
@JsonInclude(JsonInclude.Include.NON_NULL) //if the property is null, it won´t come out in the JSON
@JsonIgnoreProperties(ignoreUnknown = true) //Ignore Properties #ignoreUnkown = true : accept other properties, don't throw UnrecognizedPropertyException
public class UserDto {

    private Long id;

    @NotBlank(message = "Please provide username")
    @Size(min = 2, max = 30)
    private String username;

    @NotBlank(message = "Please provide password")
    @Size(min = 2, max = 40)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Please provide email")
    @Size(max = 35)
    @Email(message = "Please insert a good email")
    private String email;

    @NotBlank(message = "Please provide full name")
    @Size(min = 5, max = 40)
    private String fullName;

    // @NotEmpty(message = "Please provide roles")
    private Collection<RoleDto> roles;
    private String message;

}
