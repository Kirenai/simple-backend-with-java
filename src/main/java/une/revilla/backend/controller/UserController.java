package une.revilla.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.service.UserService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE
})
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        System.out.println("get");
        List<User> allUsers = this.userService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUser(@PathVariable Long id) {
        User user = this.userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        User userSaved = this.userService.saveUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @PostMapping(path = "/add/task/{id}")
    public ResponseEntity<User> addTaskUser(@PathVariable Long id, @RequestBody Task task) {
        User userWithTask = this.userService.addTaskUser(id, task);
        return new ResponseEntity<>(userWithTask, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userData) {
        User user = this.userService.updateUser(id, userData);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        User user = this.userService.deleteUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
