package une.revilla.backend.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.entity.Role;

@Component
@Scope("singleton")
@AllArgsConstructor
public class RoleMapper {
    
    private final ModelMapper modelMapper;

    public List<RoleDto> toRoleDtoList(List<Role> roles) {
        return roles.stream()
                .map(role -> this.modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    public RoleDto toRoleDto(Role role) {
        return this.modelMapper.map(role, RoleDto.class);
    }

    public Role toRole(RoleDto role) {
        return this.modelMapper.map(role, Role.class);
    }
}
