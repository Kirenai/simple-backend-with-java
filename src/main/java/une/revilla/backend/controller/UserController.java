package une.revilla.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Qualifier("userService")
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<UserDto>> getUsers(
            @PageableDefault(size = 9)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable) {
        List<UserDto> allUsers = this.userService.findAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = this.userService.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> create(
            @Valid @RequestBody UserDto userDto) {
        UserDto userSaved = this.userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        UserDto userUpdated = this.userService.updateUser(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @PutMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserByAdmin(
            @Valid @RequestBody UserDto userDto) {
        UserDto userUpdated = this.userService.updateUserByAdmin(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<UserDto> delete(@PathVariable Long id) {
        UserDto userRemoved = this.userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userRemoved);
    }

}
