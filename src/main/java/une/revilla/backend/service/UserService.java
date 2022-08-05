package une.revilla.backend.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.TaskRequest;

public interface UserService {

    List<UserDto> findAllUsers(Pageable pageable);

    UserDto findUserById(Long id);

    UserDto findByUsername(String username);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto updateUserByAdmin(UserDto userDto);

    UserDto deleteUserById(Long id);

    Boolean existsByEmail(String email);

    User updateTaskUser(Long userId, TaskRequest taskToUpdate);

}
