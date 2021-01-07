package une.revilla.backend.service;

import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User findUserById(Long id);

    User saveUser(User newUser);

    User updateUser(Long id, User userData);

    User deleteUserById(Long id);

    User addTaskUser(Long id, Task task);

}
