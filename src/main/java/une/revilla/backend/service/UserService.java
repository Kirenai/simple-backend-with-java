package une.revilla.backend.service;

import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.TaskRequest;
import une.revilla.backend.payload.request.UserRequest;
import une.revilla.backend.payload.response.MessageResponse;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);

    UserDto findByUsername(String username);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto updateUserByAdmin(Long userId, UserRequest userData);

    UserDto deleteUserById(Long id);

    MessageResponse addTaskUser(Long id, Task task);

    Boolean existsByEmail(String email);

    User updateTaskUser(Long userId, TaskRequest taskToUpdate);
}
