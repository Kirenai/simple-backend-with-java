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
        Role role = this.roleRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("The role with that id was not found"));
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
        Role role = this.roleRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("The role with that id was not found"));
        role.setName(roleDto.getName());
        Role roleSaved = this.roleRepository.save(role);
        return this.roleMapper.toRoleDto(roleSaved);
    }

    @Transactional
    @Override
    public RoleDto deleteRole(Long id) {
        RoleDto roleDtoFound = this.findRoleById(id);
        Role role = this.roleMapper.toRole(roleDtoFound);
        this.roleRepository.delete(role);
        return roleDtoFound;
    }
}
