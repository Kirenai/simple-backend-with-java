package une.revilla.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.entity.Task;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.service.TaskService;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    @Qualifier("taskService")
    private final TaskService taskService;

    @GetMapping(path = "/task")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = this.taskService.findAllTasks();
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/task/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task taskFound = this.taskService.findTaskById(id);
        return new ResponseEntity<>(taskFound, HttpStatus.OK);
    }

    @PostMapping("/task")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<Task> addTask(@RequestBody Task newTask) {
        return new ResponseEntity<>(this.taskService.saveTask(newTask), HttpStatus.CREATED);
    }

    @PutMapping("/task/{id}/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskData) {
        Task taskUpdated = this.taskService.updateTask(id, taskData);
        return new ResponseEntity<>(taskUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/task/{idTaskToDelete}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<Task> deleteTask(@PathVariable Long idTaskToDelete) {
        Task taskDeleted = this.taskService.deleteTaskById(idTaskToDelete);
        return new ResponseEntity<>(taskDeleted, HttpStatus.OK);
    }

    @DeleteMapping(path = "/task/{userId}/{idTaskToDelete}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<MessageResponse> deleteTaskByUserId(@PathVariable Long userId,
                                                @PathVariable Long idTaskToDelete) {
        MessageResponse messageResponse = this.taskService.deleteTaskByUserId(userId, idTaskToDelete);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping(path = "/task/user/{userId}")
    @PreAuthorize(value = "hasRole('ROLE_USER')")
    public ResponseEntity<List<Task>> getTasksByUserId(@PathVariable Long userId) {
        List<Task> taskByUserId = this.taskService.findTasksByUserId(userId);
        return new ResponseEntity<>(taskByUserId, HttpStatus.OK);
    }
}







