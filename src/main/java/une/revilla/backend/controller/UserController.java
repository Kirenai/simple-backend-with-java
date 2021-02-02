package une.revilla.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.RegisterRequest;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.service.TaskService;
import une.revilla.backend.service.UserService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    @Autowired
    public UserController(@Qualifier("userService") UserService userService,
                          @Qualifier("taskService") TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> allUsers = this.userService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> findUser(@PathVariable Long id) {
        User user = this.userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<User> saveUser(@RequestBody RegisterRequest registerRequest) {
        User userSaved = this.userService.saveUser(registerRequest);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PostMapping(path = "/add/task/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> addTaskUser(@PathVariable Long id, @RequestBody Task task) {
        User userWithTask = this.userService.addTaskUser(id, task);
        return new ResponseEntity<>(userWithTask, HttpStatus.OK);
    }

    @PutMapping(path = "/update/task/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<User> updateTaskUser(@PathVariable("id") Long userId,
                                               @RequestBody Task taskToUpdate) {
//        Task task = this.taskService.updateTask(id, taskToUpdate);
        User user = this.userService.updateTaskUser(userId, taskToUpdate);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update/{idRole}/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id,
                                                      @PathVariable Long idRole,
                                                      @RequestBody User userData) {
        MessageResponse message = this.userService.updateUser(id, idRole, userData);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = this.userService.deleteUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
