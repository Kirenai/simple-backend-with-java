package une.revilla.backend.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.dto.DataTaskDto;
import une.revilla.backend.dto.TaskDto;
import une.revilla.backend.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/task")
public class TaskController {

    @Qualifier("taskService")
    private final TaskService taskService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataTaskDto> findTasks() {
        List<TaskDto> allTasks = this.taskService.findAllTasks();
        DataTaskDto data = DataTaskDto.getInstance(allTasks);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<DataTaskDto> findTask(@PathVariable Long id) {
        TaskDto taskDto = this.taskService.findTaskById(id);
        DataTaskDto taskData = DataTaskDto.getInstance(taskDto);
        return ResponseEntity.status(HttpStatus.OK).body(taskData);
    }

    @GetMapping(path = "/user/{userId}")
    @PreAuthorize(value = "hasRole('USER')")
    public ResponseEntity<DataTaskDto> findTasksByUserId(@PathVariable Long userId) {
        List<TaskDto> userAllTasks = this.taskService.findTasksByUserId(userId);
        DataTaskDto userTaskData = DataTaskDto.getInstance(userAllTasks);
        return ResponseEntity.status(HttpStatus.OK).body(userTaskData);
    }

    /**
     * Save a Task for an existing User
     * @param userId The User Id
     * @param newTask The task to add to the User
     * @return A DataTaskDto with all the information
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<DataTaskDto> create(@PathVariable Long userId,
                                              @Valid @RequestBody TaskDto taskDto) {
        TaskDto tasKSaved = this.taskService.saveTask(taskDto, userId);
        DataTaskDto taskData = DataTaskDto.getInstance(tasKSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskData);
    }

    @PutMapping("/{userId}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    public ResponseEntity<DataTaskDto> update(@PathVariable Long userId,
                                              @Valid @RequestBody TaskDto taskDto) {
        TaskDto taskUpdated = this.taskService.updateTask(userId, taskDto);
        DataTaskDto taskData = DataTaskDto.getInstance(taskUpdated);
        return ResponseEntity.status(HttpStatus.OK).body(taskData);
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<DataTaskDto> delete(@PathVariable Long taskId) {
        TaskDto taskDto = this.taskService.deleteTaskById(taskId);
        DataTaskDto taskData = DataTaskDto.getInstance(taskDto);
        return ResponseEntity.status(HttpStatus.OK).body(taskData);
    }

    @DeleteMapping("/{userId}/{taskId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DataTaskDto> deleteTaskByUserId(@PathVariable Long userId,
                                                          @PathVariable Long taskId) {
        TaskDto taskDto = this.taskService.deleteTaskByUserId(userId, taskId);
        DataTaskDto taskData = DataTaskDto.getInstance(taskDto);
        return ResponseEntity.status(HttpStatus.OK).body(taskData);
    }

}







