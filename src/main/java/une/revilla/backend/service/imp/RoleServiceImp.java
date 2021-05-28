package une.revilla.backend.service.imp;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.dto.mapper.RoleMapper;
import une.revilla.backend.entity.Role;
import une.revilla.backend.exception.role.RoleNoSuchElementException;
import une.revilla.backend.repository.RoleRepository;
import une.revilla.backend.service.RoleService;

@Service
@RequiredArgsConstructor
@Qualifier("roleService")
public class RoleServiceImp implements RoleService {

    @Qualifier("roleRepository")
    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public List<RoleDto> findRoles() {
        List<Role> roles = this.roleRepository.findAll();
        return this.roleMapper.toRoleDtoList(roles);
    }

    @Override
    public RoleDto findRoleById(Long id) {
        Role role = this.findRole(id);
        return this.roleMapper.toRoleDto(role);
    }

    @Transactional
    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = this.roleMapper.toRole(roleDto);
        Role savedRole = this.roleRepository.save(role);
        return this.roleMapper.toRoleDto(savedRole);
    }

    @Transactional
    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role role = this.findRole(id);
        role.setName(roleDto.getName());
        Role roleSaved = this.roleRepository.save(role);
        return this.roleMapper.toRoleDto(roleSaved).setMessage("The role has been updated successfully");
    }

    @Transactional
    @Override
    public RoleDto deleteRole(Long id) {
        this.roleRepository.delete(this.findRole(id));
        RoleDto roleDto = new RoleDto().setMessage("The role has beed removed successfully");
        return roleDto;
    }

    /**
     * Find one role entity
     * 
     * @param id To find role persistences
     * @return A Role entity found with id
     */
    private Role findRole(Long id) {
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleNoSuchElementException("The role with that id was not found"));
    }
}
