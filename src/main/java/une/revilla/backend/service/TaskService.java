package une.revilla.backend.service;

import une.revilla.backend.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> findAllTasks();

    Task findTaskById(Long id);

    Task saveTask(Task newTask);

    Task updateTask(Long id, Task taskData);

    Task deleteTaskById(Long id);

}
