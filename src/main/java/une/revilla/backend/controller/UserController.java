package une.revilla.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.TaskRequest;
import une.revilla.backend.payload.request.UserRequest;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Qualifier("userService")
    private final UserService userService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataUserDto> findAllUsers() {
        List<UserDto> allUsers = this.userService.findAllUsers();
        DataUserDto data = new DataUserDto(null, allUsers);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataUserDto> findOneUser(@PathVariable Long id) {
        UserDto userDto = this.userService.findUserById(id);
        DataUserDto personData = new DataUserDto(userDto, null);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DataUserDto> saveUser(@Validated @RequestBody UserDto userDto) {
        UserDto userSaved = this.userService.saveUser(userDto);
        DataUserDto personData = new DataUserDto(userSaved, null);
        return new ResponseEntity<>(personData, HttpStatus.CREATED);
    }

    // TODO: update later with DTO
    @PostMapping("/task/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<MessageResponse> addTaskUser(@PathVariable Long id,
                                                       @RequestBody Task task) {
        MessageResponse message = this.userService.addTaskUser(id, task);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // TODO: update later with DTO
    @PutMapping("/task/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<User> updateTaskUser(@PathVariable("id") Long userId,
                                               @RequestBody TaskRequest taskToUpdate) {
        User user = this.userService.updateTaskUser(userId, taskToUpdate);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataUserDto> updateUser(@PathVariable Long id,
                                              @Validated @RequestBody UserDto userDto) {
        UserDto userSaved = this.userService.updateUser(id, userDto);
        DataUserDto personData = new DataUserDto(userSaved, null);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

    // TODO: update later with DTO
    @PutMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserByAdmin(@PathVariable Long userId,
                                                      @RequestBody UserRequest userRequest) {
        UserDto userDto = this.userService.updateUserByAdmin(userId, userRequest);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataUserDto> deleteUser(@PathVariable Long id) {
        UserDto userRemoved = this.userService.deleteUserById(id);
        DataUserDto personData = new DataUserDto(userRemoved, null);
        return ResponseEntity.status(HttpStatus.OK).body(personData);
    }

}
