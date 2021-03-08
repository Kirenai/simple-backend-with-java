package une.revilla.backend.service;

import une.revilla.backend.entity.Task;
import une.revilla.backend.payload.response.MessageResponse;

import java.util.List;

public interface TaskService {

    List<Task> findAllTasks();

    Task findTaskById(Long id);

    Task saveTask(Task newTask);

    Task updateTask(Long id, Task taskData);

    Task deleteTaskById(Long idTaskToDelete);

    MessageResponse deleteTaskByUserId(Long userId, Long idTaskToDelete);

    List<Task> findTasksByUserId(Long userId);
}
