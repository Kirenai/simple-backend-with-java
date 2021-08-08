package une.revilla.backend.service;

import une.revilla.backend.dto.TaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> findAllTasks();

    TaskDto findTaskById(Long id);

    List<TaskDto> findTasksByUserId(Long userId);

    TaskDto saveTask(TaskDto newTask, Long userId);

    TaskDto updateTask(Long id, TaskDto taskData);

    TaskDto deleteTaskById(Long taskId);

    TaskDto deleteTaskByUserId(Long userId, Long taskId);

}
