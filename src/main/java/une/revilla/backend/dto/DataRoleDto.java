package une.revilla.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataRoleDto {

    private RoleDto roleData;

    private List<RoleDto> data;

    private static DataRoleDto dataRoleDto = null;

    public static DataRoleDto getInstance(RoleDto roleDto) {
        if (dataRoleDto == null) {
            dataRoleDto = new DataRoleDto(roleDto, null);
        }
        dataRoleDto.setRoleData(roleDto);
        dataRoleDto.setData(null);
        return dataRoleDto;
    }

    public static DataRoleDto getInstance(List<RoleDto> data) {
        if (dataRoleDto == null) {
            dataRoleDto = new DataRoleDto(null, data);
        }
        dataRoleDto.setRoleData(null);
        dataRoleDto.setData(data);
        return dataRoleDto;
    }

}
