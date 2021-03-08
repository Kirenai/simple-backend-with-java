package une.revilla.backend.service.imp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.dto.mapper.UserMapper;
import une.revilla.backend.entity.Role;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.enums.RoleEnum;
import une.revilla.backend.exception.user.UserNoSuchElementException;
import une.revilla.backend.payload.request.RegisterRequest;
import une.revilla.backend.payload.request.TaskRequest;
import une.revilla.backend.payload.request.UserRequest;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.repository.RoleRepository;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("userService")
public class UserServiceImp implements UserService {

    @Qualifier("userRepository")
    private final UserRepository userRepository;
    @Qualifier("taskRepository")
    private final TaskRepository taskRepository;
    @Qualifier("roleRepository")
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Returns a list of all users
     * @return List<UserDto>
     */
    @Override
    public List<UserDto> findAllUsers() {
        return UserMapper.toUserDtoList(this.userRepository.findAll());
    }

    @Override
    public UserDto findUserById(Long id) {
        return UserMapper.toUserDto(this.getUserById(id));
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow();
        return UserMapper.toUserDto(user);
    }

    /**
     * User registration by administrator
     * This method is called only when the administrator needs add new user
     *
     * @param registerRequest The data of the new user
     * @return The user saved
     */
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
        return this.userRepository.findById(userSaved.getId()).orElseThrow();
    }

    @Override
    public UserDto updateUser(Long id, UserRequest userRequest) {
        User userToUpdate = this.transformToUser(id, userRequest);

        Set<Role> roleUser = new HashSet<>(Set.of(this.insertRoles("user")));
        userToUpdate.setRoles(roleUser);

        return UserMapper.toUserDto(this.userRepository.save(userToUpdate))
                .setMessage("User updated successfully");
    }

    /**
     * Returns a DTO of the user entity with all its updated information
     *
     * @param userId id of the User to update
     * @param userData User information to change
     * @return A UserDto
     */
    @Override
    public UserDto updateUserByAdmin(Long userId, UserRequest userData) {
        return this.updateByAdmin(userId, userData);
    }

    @Override
    public User updateTaskUser(Long userId, TaskRequest taskToUpdate) {
        User user = this.getUserById(userId);

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
    public MessageResponse deleteUserById(Long id) {
        this.userRepository.delete(this.getUserById(id));
        return new MessageResponse("User deleted successfully");
    }

    @Override
    public MessageResponse addTaskUser(Long id, Task task) {
        User user = this.getUserById(id);
        task.setUser(user);
        this.taskRepository.save(task);
        return new MessageResponse("New task added to user");
    }

    /**
     * Returned true if parameter passed is in the DB
     *
     * @param email Parameter to search in the DB
     * @return true or false, whether it exists or not
     */
    @Override
    public Boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    /**
     * Returns the user's information by a given id
     *
     * @param id Required parameter to find the entity
     * @return A User Entity
     */
    private User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNoSuchElementException("User not found with id: " + id));
    }

    /**
     * Returns a DTO with the necessary information for the ADMIN
     *
     * @param userId   Of the user that will be updated
     * @param userData The data to update
     * @return A UserDto
     */
    private UserDto updateByAdmin(Long userId, UserRequest userData) {
        User userToUpdate = this.transformToUser(userId, userData);

        Set<Role> newRoles = userData.getRoles()
                .stream()
                .map(this::insertRoles)
                .collect(Collectors.toSet());

        if (newRoles.isEmpty()) {
            Set<Role> roleUser = new HashSet<>(Set.of(this.insertRoles("user")));
            userToUpdate.setRoles(roleUser);
        } else {
            userToUpdate.setRoles(newRoles);
        }

        return UserMapper.toUserDto(this.userRepository.save(userToUpdate))
                .setMessage("User updated by an Administrator");
    }

    /**
     * Returns the information transformed from the received data to a User object
     *
     * @param userRequest Incoming information for the User Object
     * @return A User Entity with data
     */
    private User transformToUser(Long id, UserRequest userRequest) {
        User user = this.getUserById(id);

        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());

        return user;
    }

    /**
     * Returns a selected role from the DB of the parameter provided for the user
     *
     * @param role Parameter that has the information of the role to search
     * @return A role for the User
     */
    private Role insertRoles(String role) {
        if (role.equals("admin")) {
            return this.roleRepository.findByName(RoleEnum.ADMIN.getRole())
                    .orElseThrow(() -> new IllegalStateException("Role not found"));
        } else if (role.equals("mod")) {
            return this.roleRepository.findByName(RoleEnum.MODERATOR.getRole())
                    .orElseThrow(() -> new IllegalStateException("Role not found"));
        } else {
            return this.roleRepository.findByName(RoleEnum.USER.getRole())
                    .orElseThrow(() -> new IllegalStateException("Role not found"));
        }
    }
}
