package une.revilla.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.service.RoleService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/role")
public class RoleController {

    @Qualifier("roleService")
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDto>> getRoles() {
        List<RoleDto> allRoles = this.roleService.findRoles();
        return ResponseEntity.status(HttpStatus.OK).body(allRoles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> getRole(@PathVariable Long id) {
        RoleDto roleDto = this.roleService.findRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(roleDto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> create(@Valid @RequestBody RoleDto roleDto) {
        RoleDto savedRole = this.roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @Valid @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = this.roleService.updateRole(id, roleDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> delete(@PathVariable Long id) {
        RoleDto roleRemoved = this.roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(roleRemoved);
    }
}
