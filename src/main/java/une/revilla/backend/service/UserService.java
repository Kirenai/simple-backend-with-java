package une.revilla.backend.service;

import org.springframework.data.domain.Pageable;
import une.revilla.backend.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers(Pageable pageable);

    UserDto findUserById(Long id);

    UserDto findByUsername(String username);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto updateUserByAdmin(UserDto userDto);

    UserDto deleteUserById(Long id);

    Boolean existsByEmail(String email);

}
