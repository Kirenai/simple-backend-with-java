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
import une.revilla.backend.dto.TaskDto;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.service.TaskService;
import une.revilla.backend.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @Qualifier("userService")
    private final UserService userService;
    @Qualifier("taskService")
    private final TaskService taskService;

    /**
     * Entry point to get paged users
     * Authorization ADMIN MODERATOR
     *
     * @param pageable paging rules
     * @return {@link UserDto} List
     */
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

    /**
     * Entry point to get user by id
     * Authorization ADMIN MODERATOR
     *
     * @param id to search for the user
     * @return {@link UserDto}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        UserDto userDto = this.userService.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    /**
     * Entry point to get user task
     * Authorization ADMIN MODERATOR USER
     *
     * @param id for task search
     * @return {@link TaskDto} List
     */
    @GetMapping("/{id}/tasks")
    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    public ResponseEntity<List<TaskDto>> getTasksByUserId(
            @PathVariable Long id) {
        List<TaskDto> userTasks = this.taskService.findTasksByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(userTasks);
    }

    /**
     * Entry point to create a new {@link une.revilla.backend.entity.User}
     * Authorization ADMIN
     *
     * @param userDto input data
     * @return {@link UserDto}
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> create(
            @Valid @RequestBody UserDto userDto) {
        UserDto userSaved = this.userService.saveUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    /**
     * Entry point to update an existing {@link une.revilla.backend.entity.User}
     * Authorization USER
     *
     * @param id of {@link une.revilla.backend.entity.User} to update
     * @param userDto update data
     * @return {@link UserDto}
     */
    @PutMapping("/{id}/edit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDto> update(@PathVariable Long id,
            @Valid @RequestBody UserDto userDto) {
        UserDto userUpdated = this.userService.updateUser(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    /**
     * Entry point to update an {@link une.revilla.backend.entity.User} by ADMIN
     * Authorization ADMIN
     *
     * @param userDto update data
     * @return
     */
    @PutMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserByAdmin(
            @Valid @RequestBody UserDto userDto) {
        UserDto userUpdated = this.userService.updateUserByAdmin(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    /**
     * Entry point to delete a {@link une.revilla.backend.entity.User}
     * Authorization ADMIN MODERATOR
     *
     * @param id of user to delete
     * @return {@link UserDto} message
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<UserDto> delete(@PathVariable Long id) {
        UserDto userRemoved = this.userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userRemoved);
    }

    /**
     * Entry point to delete a user {@link une.revilla.backend.entity.Task} by
     * user id, Authorization USER
     *
     * @param userId to search user
     * @param taskId to delete the task
     * @return {@link TaskDto} message
     */
    @DeleteMapping("/{userId}/tasks/{taskId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskDto> deleteTaskByUserId(@PathVariable Long userId,
            @PathVariable Long taskId) {
        TaskDto taskDto = this.taskService.deleteTaskByUserId(userId, taskId);
        return ResponseEntity.status(HttpStatus.OK).body(taskDto);
    }

}
