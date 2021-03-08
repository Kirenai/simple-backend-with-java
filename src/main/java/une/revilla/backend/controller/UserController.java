package une.revilla.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.RegisterRequest;
import une.revilla.backend.payload.request.TaskRequest;
import une.revilla.backend.payload.request.UserRequest;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.service.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @Qualifier("userService")
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<List<?>> findAllUsers() {
        List<UserDto> allUsers = this.userService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> findOneUser(@PathVariable Long id) {
        UserDto userDto = this.userService.findUserById(id);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<User> saveUser(@RequestBody RegisterRequest registerRequest) {
        User userSaved = this.userService.saveUser(registerRequest);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PostMapping(path = "/add/task/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<MessageResponse> addTaskUser(@PathVariable Long id,
                                                       @RequestBody Task task) {
        MessageResponse message = this.userService.addTaskUser(id, task);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping(path = "/update/task/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> updateTaskUser(@PathVariable("id") Long userId,
                                               @RequestBody TaskRequest taskToUpdate) {
        User user = this.userService.updateTaskUser(userId, taskToUpdate);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserRequest userRequest) {
        UserDto userDto = this.userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/admin/update/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> updateUserByAdmin(@PathVariable Long userId,
                                                      @RequestBody UserRequest userRequest) {
        UserDto userDto = this.userService.updateUserByAdmin(userId, userRequest);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        MessageResponse message = this.userService.deleteUserById(id);
        return ResponseEntity.ok(message);
    }

}
