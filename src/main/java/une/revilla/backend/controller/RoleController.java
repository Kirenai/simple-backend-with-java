package une.revilla.backend.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import une.revilla.backend.dto.DataRoleDto;
import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.service.RoleService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

    @Qualifier("roleService")
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataRoleDto> getRoles() {
        List<RoleDto> allRoles = this.roleService.findRoles();
        DataRoleDto data = DataRoleDto.getInstance(allRoles);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataRoleDto> getRole(@PathVariable Long id) {
        RoleDto roleDto = this.roleService.findRoleById(id);
        DataRoleDto roleData = DataRoleDto.getInstance(roleDto);
        return ResponseEntity.status(HttpStatus.OK).body(roleData);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataRoleDto> create(@Valid @RequestBody RoleDto roleDto) {
        RoleDto savedRole = this.roleService.createRole(roleDto);
        DataRoleDto roleData = DataRoleDto.getInstance(savedRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleData);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataRoleDto> update(@PathVariable Long id, @Valid @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = this.roleService.updateRole(id, roleDto);
        DataRoleDto dataRole = DataRoleDto.getInstance(updatedRole);
        return ResponseEntity.status(HttpStatus.OK).body(dataRole);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataRoleDto> delete(@PathVariable Long id) {
        RoleDto roleRemoved = this.roleService.deleteRole(id);
        DataRoleDto dataRole = DataRoleDto.getInstance(roleRemoved);
        return ResponseEntity.status(HttpStatus.OK).body(dataRole);
    }
}
