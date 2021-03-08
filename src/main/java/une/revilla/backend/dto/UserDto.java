package une.revilla.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import une.revilla.backend.entity.Role;
import une.revilla.backend.entity.Task;

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
    private String username;
    private String email;
    private String fullName;
    private Collection<Role> roles;
    private String message;

}
