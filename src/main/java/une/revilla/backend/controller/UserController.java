package une.revilla.backend.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.dto.DataUserDto;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Qualifier("userService")
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataUserDto> getUsers(
            @PageableDefault(page = 0, size = 9)
            @SortDefault.SortDefaults({
                @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            }) Pageable pageable) {
        List<UserDto> allUsers = this.userService.findAllUsers(pageable);
        DataUserDto data = DataUserDto.getInstance(allUsers);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataUserDto> getUser(@PathVariable Long id) {
        UserDto userDto = this.userService.findUserById(id);
        DataUserDto personData = DataUserDto.getInstance(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DataUserDto> create(
            @Valid @RequestBody UserDto userDto) {
        UserDto userSaved = this.userService.saveUser(userDto);
        DataUserDto personData = DataUserDto.getInstance(userSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(personData);
    }

    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataUserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        UserDto userSaved = this.userService.updateUser(id, userDto);
        DataUserDto personData = DataUserDto.getInstance(userSaved);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

    @PutMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataUserDto> updateUserByAdmin(
            @Valid @RequestBody UserDto userDto) {
        UserDto userUpdate = this.userService.updateUserByAdmin(userDto);
        DataUserDto personData = DataUserDto.getInstance(userUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataUserDto> delete(@PathVariable Long id) {
        UserDto userRemoved = this.userService.deleteUserById(id);
        DataUserDto personData = DataUserDto.getInstance(userRemoved);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

}
