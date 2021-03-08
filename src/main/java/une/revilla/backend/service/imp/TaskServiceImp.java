package une.revilla.backend.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.exception.task.TaskNoSuchElementException;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.TaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("taskService")
public class TaskServiceImp implements TaskService {

    @Qualifier("taskRepository")
    private final TaskRepository taskRepository;
    @Qualifier("userRepository")
    private final UserRepository userRepository;

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task findTaskById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() -> new TaskNoSuchElementException("Task no found " + id));
    }

    @Override
    public Task saveTask(Task newTask) {
        Task taskSave = taskRepository.save(newTask);
        return this.findTaskById(taskSave.getId());
    }

    @Override
    public Task updateTask(Long id, Task taskData) {
        Task taskToUpdate = this.findTaskById(id);
        taskToUpdate.setTitle(taskData.getTitle());
        taskToUpdate.setDescription(taskData.getDescription());
        taskToUpdate.setAuthor(taskData.getAuthor());
        taskRepository.save(taskToUpdate);
        return this.findTaskById(id);
    }

    @Override
    public Task deleteTaskById(Long idTaskToDelete) {
        Task taskFound = this.taskRepository.findById(idTaskToDelete)
                .orElseThrow(() -> new TaskNoSuchElementException("Task not found"));
        this.taskRepository.delete(taskFound);
        return taskFound;
    }

    @Override
    public MessageResponse deleteTaskByUserId(Long userId, Long idTaskToDelete) {
        String message = "User hasn't that task";
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User doesn't found"));
        boolean isTaskPresent = user.getTasks().stream()
                .anyMatch(task -> task.getId().equals(idTaskToDelete));
        if (isTaskPresent) {
            this.taskRepository.deleteById(idTaskToDelete);
            message = "User has that task: the task has been deleted";
        }
        return new MessageResponse(message);
    }

    @Override
    public List<Task> findTasksByUserId(Long userId) {
        return this.taskRepository.findTasksByUserId(userId)
                .orElseThrow(() -> new TaskNoSuchElementException(
                        "No tasks found for this User with id: " + userId
                ));
    }
}

