package une.revilla.backend.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import une.revilla.backend.dto.TaskDto;
import une.revilla.backend.dto.mapper.TaskMapper;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.enums.task.TaskMessageEnum;
import une.revilla.backend.exception.task.TaskNoSuchElementException;
import une.revilla.backend.exception.user.UserNoSuchElementException;
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
    private final TaskMapper taskMapper;

    @Override
    public List<TaskDto> findAllTasks() {
        return this.taskMapper.toTaskDtoList(this.taskRepository.findAll());
    }

    @Override
    public TaskDto findTaskById(Long id) {
        Task task = this.getTaskById(id);
        return this.taskMapper.toTaskDto(task);
    }

    @Override
    public TaskDto saveTask(TaskDto taskDto, Long userId) {
        Task taskToSave = this.taskMapper.toTask(taskDto);
        User userFound = this.getUserById(userId);
        taskToSave.setUser(userFound);
        return this.taskMapper.toTaskDto(this.taskRepository.save(taskToSave));
    }

    @Override
    public TaskDto updateTask(Long userId, TaskDto taskData) {
        User userToUpdateTask = this.getUserById(userId);
        boolean hasTask = userToUpdateTask.getTasks().stream().anyMatch(it -> it.getId().equals(taskData.getId()));
        if (hasTask) {
            Task taskToUpdate = this.getTaskById(taskData.getId());
            taskToUpdate.setTitle(taskData.getTitle());
            taskToUpdate.setDescription(taskData.getDescription());
            taskToUpdate.setAuthor(taskData.getAuthor());
            return this.taskMapper.toTaskDto(this.taskRepository.save(taskToUpdate))
                    .setMessage(TaskMessageEnum.UPDATED_TASK.getMessage());
        }
        return new TaskDto().setMessage(TaskMessageEnum.DOES_NOT_TASK_USER.getMessage());
    }

    @Override
    public TaskDto deleteTaskById(Long taskId) {
        Task taskFound = this.getTaskById(taskId);
        this.taskRepository.delete(taskFound);
        return new TaskDto().setMessage(TaskMessageEnum.REMOVED_BY_ADMIN_MODERATOR.getMessage());
    }

    @Override
    public TaskDto deleteTaskByUserId(Long userId, Long taskId) {
        String message = TaskMessageEnum.DOES_NOT_TASK_USER.getMessage();
        User user = this.getUserById(userId);
        boolean hasTask = user.getTasks().stream().anyMatch(task -> task.getId().equals(taskId));
        if (hasTask) {
            this.taskRepository.deleteById(taskId);
            message = TaskMessageEnum.REMOVED_BY_USER.getMessage();
        }
        return new TaskDto().setMessage(message);
    }

    @Override
    public List<TaskDto> findTasksByUserId(Long userId) {
        List<Task> userTasksList = this.taskRepository.findTasksByUserId(userId).orElseThrow(
                () -> new TaskNoSuchElementException(TaskMessageEnum.TASKS_NOT_FOUND.getMessage() + userId));
        return this.taskMapper.toTaskDtoList(userTasksList);
    }

    private Task getTaskById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() -> new TaskNoSuchElementException(TaskMessageEnum.TASK_NOT_FOUND.getMessage() + id));
    }

    /**
     * Returns the user's information by a given id
     *
     * @param id Required parameter to find the entity
     * @return A User Entity
     */
    private User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNoSuchElementException(TaskMessageEnum.USER_NOT_FOUND.getMessage() + id));
    }
}
