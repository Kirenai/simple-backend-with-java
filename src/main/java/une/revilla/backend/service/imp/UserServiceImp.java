package une.revilla.backend.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import une.revilla.backend.entity.Role;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.enums.RoleEnum;
import une.revilla.backend.payload.request.RegisterRequest;
import une.revilla.backend.repository.RoleRepository;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("userService")
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(@Qualifier("userRepository") UserRepository userRepository,
                          @Qualifier("taskRepository") TaskRepository taskRepository,
                          @Qualifier("roleRepository") RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
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
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow();
    }

    @Override
    public User saveUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());

        Set<String> rolesRequest = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (rolesRequest == null) {
            Role roleUser = this.roleRepository.findByName(RoleEnum.USER.getRole()).orElseThrow();
            roles.add(roleUser);
        } else {
            rolesRequest.forEach(role -> {
                if (role.equals("admin")) {
                    Role roleAdmin = this.roleRepository.findByName(RoleEnum.ADMIN.getRole())
                            .orElseThrow();
                    roles.add(roleAdmin);
                } else if (role.equals("mod")) {
                    Role roleModerator = this.roleRepository.findByName(RoleEnum.MODERATOR.getRole())
                            .orElseThrow();
                    roles.add(roleModerator);
                } else {
                    Role roleUser = this.roleRepository.findByName(RoleEnum.USER.getRole())
                            .orElseThrow();
                    roles.add(roleUser);
                }
            });
        }

        user.setRoles(roles);
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
    public User updateTaskUser(Long userId, Task taskToUpdate) {
        User user = this.userRepository.findById(userId)
                .orElseThrow();

        Collection<Task> tasks = user.getTasks();
        Collection<Task> tasksUpdate = new ArrayList<>();

        tasks.forEach(task -> {
            if (task.getId().equals(taskToUpdate.getId())) {
                task.setTitle(taskToUpdate.getTitle());
                task.setAuthor(taskToUpdate.getAuthor());
                task.setDescription(taskToUpdate.getDescription());
            }
            tasksUpdate.add(task);
        });

        user.setTasks(tasksUpdate);
        return this.userRepository.save(user);
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

    @Override
    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

}
