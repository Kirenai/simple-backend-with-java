package une.revilla.backend.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import une.revilla.backend.entity.Task;
import une.revilla.backend.exception.task.TaskNoSuchElementException;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.service.TaskService;

import java.util.List;

@Service
@Qualifier("taskService")
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImp(@Qualifier("taskRepository") TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

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
    public Task deleteTaskById(Long id) {
        Task taskFound = this.findTaskById(id);
        this.taskRepository.delete(taskFound);
        return taskFound;
    }

    @Override
    public List<Task> findTaskByUserId(Long id) {
        return this.taskRepository.findTaskByUserId(id)
                .orElseThrow();
    }
}

