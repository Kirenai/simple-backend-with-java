package une.revilla.backend.dto.mapper;

import une.revilla.backend.dto.TaskDto;
import une.revilla.backend.entity.Task;

public class TaskMapper {

    public static TaskDto toTaskDto(Task task) {
        return new TaskDto()
                .setId(task.getId())
                .setTitle(task.getTitle())
                .setAuthor(task.getAuthor())
                .setDescription(task.getDescription());
    }

}
