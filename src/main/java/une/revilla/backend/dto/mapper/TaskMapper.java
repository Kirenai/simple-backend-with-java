package une.revilla.backend.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.dto.TaskDto;
import une.revilla.backend.entity.Task;

@Component
@Scope("singleton")
@RequiredArgsConstructor
public class TaskMapper {

    private final ModelMapper modelMapper;

    public List<TaskDto> toTaskDtoList(List<Task> taskList) {
        return taskList
                .stream()
                .map(task -> this.modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    public TaskDto toTaskDto(Task task) {
        return this.modelMapper.map(task, TaskDto.class);
    }

    public Task toTask(TaskDto taskDto) {
        return this.modelMapper.map(taskDto, Task.class);
    }

}
