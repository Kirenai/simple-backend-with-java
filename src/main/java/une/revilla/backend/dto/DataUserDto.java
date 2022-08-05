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

    private UserDto personData;

    private List<UserDto> data;

    private static DataUserDto dataUserDto = null;

    public static DataUserDto getInstance(UserDto personData) {
        if (dataUserDto == null) {
            dataUserDto = new DataUserDto(personData, null);
        }
        dataUserDto.setPersonData(personData);
        dataUserDto.setData(null);
        return dataUserDto;
    }

    public static DataUserDto getInstance(List<UserDto> data) {
        if (dataUserDto == null) {
            dataUserDto = new DataUserDto(null, data);
        }
        dataUserDto.setData(data);
        dataUserDto.setPersonData(null);
        return dataUserDto;
    }

}
