package une.revilla.backend.dto;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Collection<TaskDto> tasks;
    private Collection<RoleDto> roles;

    private String message;

}
