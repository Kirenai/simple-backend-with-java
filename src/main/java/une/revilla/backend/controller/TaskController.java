package une.revilla.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.entity.Task;
import une.revilla.backend.service.TaskService;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods = {
        RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE
})
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(@Qualifier("taskService") TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> allTasks = this.taskService.findAllTasks();
        return new ResponseEntity<>(allTasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task taskFound = this.taskService.findTaskById(id);
        return new ResponseEntity<>(taskFound, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task newTask) {
        return new ResponseEntity<>(this.taskService.saveTask(newTask), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskData) {
        Task taskUpdated = this.taskService.updateTask(id, taskData);
        return new ResponseEntity<>(taskUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        Task taskDeleted = this.taskService.deleteTaskById(id);
        return new ResponseEntity<>(taskDeleted, HttpStatus.OK);
    }
}







