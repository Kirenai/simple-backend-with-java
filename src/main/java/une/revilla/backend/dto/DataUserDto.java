package une.revilla.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataUserDto {

    private final UserDto personData;

    private final List<UserDto> data;

    private static DataUserDto instance = null;

    public static DataUserDto getInstance(UserDto personData) {
        if (instance != null) {
            return instance;
        }
        return new DataUserDto(personData, null);
    }

    public static DataUserDto getInstance(List<UserDto> data) {
        if (instance != null) {
            return instance;
        }
        return new DataUserDto(null, data);
    }
    
}