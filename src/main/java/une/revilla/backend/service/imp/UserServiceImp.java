package une.revilla.backend.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import une.revilla.backend.entity.Role;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.UserService;

import java.util.List;
import java.util.Set;

@Service
@Qualifier("userService")
public class UserServiceImp implements UserService {

    private final String USER_ROLE = "ROLE_USER";

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(@Qualifier("userRepository") UserRepository userRepository,
                          @Qualifier("taskRepository") TaskRepository taskRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public User saveUser(User newUser) {
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setFullName(newUser.getFullName());
        Role role = new Role();
        role.setName(USER_ROLE);
        user.setRoles(Set.of(role));
        User userSaved = this.userRepository.save(user);
        return this.findUserById(userSaved.getId());
    }

    @Override
    public User updateUser(Long id, User userData) {
        User userToUpdate = this.findUserById(id);
        userToUpdate.setUsername(userData.getUsername());
        userToUpdate.setPassword(passwordEncoder.encode(userData.getPassword()));
        userToUpdate.setEmail(userData.getEmail());
        userToUpdate.setFullName(userData.getFullName());
        return this.userRepository.save(userToUpdate);
    }

    @Override
    public User deleteUserById(Long id) {
        User userToDelete = this.userRepository.findById(id).orElseThrow();
        this.userRepository.delete(userToDelete);
        return userToDelete;
    }

    @Override
    public User addTaskUser(Long id, Task task) {
        User user = this.userRepository.findById(id)
                .orElseThrow();
        task.setUser(user);
        this.taskRepository.save(task);
        return user;
    }

}
