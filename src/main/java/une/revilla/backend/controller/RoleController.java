package une.revilla.backend.controller;

import java.util.List;

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
    public ResponseEntity<List<RoleDto>> findRoles() {
        return ResponseEntity.ok().body(this.roleService.findRoles());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> findOneRole(@PathVariable Long id) {
        RoleDto role = this.roleService.findRoleById(id);
        return ResponseEntity.ok().body(role);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto) {
        RoleDto savedRole = this.roleService.createRole(roleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Long id, @RequestBody RoleDto roleDto) {
        RoleDto updatedRole = this.roleService.updateRole( id, roleDto);
        return ResponseEntity.ok().body(updatedRole);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> deleteRole(@PathVariable Long id) {
        RoleDto roleRemoved = this.roleService.deleteRole(id);
        return ResponseEntity.ok().body(roleRemoved);
    }
}
