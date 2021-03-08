package une.revilla.backend.service;

import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.RegisterRequest;
import une.revilla.backend.payload.request.TaskRequest;
import une.revilla.backend.payload.request.UserRequest;
import une.revilla.backend.payload.response.MessageResponse;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);

    UserDto findByUsername(String username);

    User saveUser(RegisterRequest registerRequest);

    UserDto updateUser(Long id, UserRequest userRequest);

    UserDto updateUserByAdmin(Long userId, UserRequest userData);

    MessageResponse deleteUserById(Long id);

    MessageResponse addTaskUser(Long id, Task task);

    Boolean existsByEmail(String email);

    User updateTaskUser(Long userId, TaskRequest taskToUpdate);
}
