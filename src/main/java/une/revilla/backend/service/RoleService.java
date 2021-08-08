package une.revilla.backend.service;

import java.util.List;

import une.revilla.backend.dto.RoleDto;

public interface RoleService {

    List<RoleDto> findRoles();

    RoleDto findRoleById(Long id);

    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(Long id, RoleDto roleDto);

    RoleDto deleteRole(Long id);
}
