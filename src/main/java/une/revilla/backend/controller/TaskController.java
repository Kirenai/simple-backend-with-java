package une.revilla.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.dto.TaskDto;
import une.revilla.backend.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    @Qualifier("taskService")
    private final TaskService taskService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<TaskDto>> getTasks() {
        List<TaskDto> allTasks = this.taskService.findAllTasks();
        return ResponseEntity.status(HttpStatus.OK).body(allTasks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        TaskDto taskDto = this.taskService.findTaskById(id);
        return ResponseEntity.status(HttpStatus.OK).body(taskDto);
    }

    /**
     * Save a Task for an existing User
     *
     * @param userId  The User Id
     * @param taskDto The taskDto to add to the User
     * @return A DataTaskDto with all the information
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<TaskDto> create(@PathVariable Long userId,
                                          @Valid @RequestBody TaskDto taskDto) {
        TaskDto tasKSaved = this.taskService.saveTask(taskDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tasKSaved);
    }

    @PutMapping("/{userId}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<TaskDto> update(@PathVariable Long userId,
                                          @Valid @RequestBody TaskDto taskDto) {
        TaskDto taskUpdated = this.taskService.updateTask(userId, taskDto);
        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<TaskDto> delete(@PathVariable Long taskId) {
        TaskDto taskDto = this.taskService.deleteTaskById(taskId);
        return ResponseEntity.status(HttpStatus.OK).body(taskDto);
    }

}







