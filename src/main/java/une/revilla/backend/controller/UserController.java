package une.revilla.backend.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.dto.DataDto;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.RegisterRequest;
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
    public ResponseEntity<DataDto>indAllUsers() {
        List<UserDto> allUsers = this.userService.findAllUsers();
        DataDto data = new DataDto(null, allUsers);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataDto> findOneUser(@PathVariable Long id) {
        UserDto userDto = this.userService.findUserById(id);
        DataDto personData = new DataDto(userDto, null);
        return new ResponseEntity<>(personData, HttpStatus.OK);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<RegisterRequest> saveUser(@Validated @RequestBody RegisterRequest registerRequest) {
//        User userSaved = this.userService.saveUser(registerRequest);
//        UserDto userDto = this.userMapper.toUserDto(userSaved);
//        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        return ResponseEntity.created(URI.create("Created")).body(registerRequest);
    }

    @PostMapping("/user2")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUserDto(@Validated @RequestBody UserDto userDto) {
        System.out.println(userDto);
        return userDto;
    }

    @PostMapping("/task/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<MessageResponse> addTaskUser(@PathVariable Long id,
                                                       @RequestBody Task task) {
        MessageResponse message = this.userService.addTaskUser(id, task);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/task/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<User> updateTaskUser(@PathVariable("id") Long userId,
                                               @RequestBody TaskRequest taskToUpdate) {
        User user = this.userService.updateTaskUser(userId, taskToUpdate);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserRequest userRequest) {
        UserDto userDto = this.userService.updateUser(id, userRequest);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserByAdmin(@PathVariable Long userId,
                                                      @RequestBody UserRequest userRequest) {
        UserDto userDto = this.userService.updateUserByAdmin(userId, userRequest);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
        MessageResponse message = this.userService.deleteUserById(id);
        return ResponseEntity.ok(message);
    }

}
