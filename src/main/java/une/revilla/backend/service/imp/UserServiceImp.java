package une.revilla.backend.service.imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.dto.mapper.UserMapper;
import une.revilla.backend.entity.Role;
import une.revilla.backend.entity.Task;
import une.revilla.backend.entity.User;
import une.revilla.backend.enums.RoleEnum;
import une.revilla.backend.exception.user.UserNoSuchElementException;
import une.revilla.backend.payload.request.TaskRequest;
import une.revilla.backend.payload.request.UserRequest;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.repository.RoleRepository;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.UserService;

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
    private final UserMapper userMapper;

    /**
     * Returns a list of all users
     * @return List<UserDto>
     */
    @Override
    public List<UserDto> findAllUsers() {
        return this.userMapper.toUserDtoList(this.userRepository.findAll());
    }

    @Override
    public UserDto findUserById(Long id) {
        return this.userMapper.toUserDto(this.getUserById(id));
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow();
        return this.userMapper.toUserDto(user);
    }

    /**
     * User registration by administrator
     * This method is called only when the administrator needs add new user
     *
     * @param UserDto The Data Transfer Object of the new User persistence 
     * @return returns a UserDTO object with the user's data
     */
    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());

        Set<String> rolesDto = userDto.getRoles()
                .stream()
                .map(RoleDto::getName)
                .collect(Collectors.toSet());
        Set<Role> roles = new HashSet<>();

        if (rolesDto.size() == 0) {
            Role roleUser = this.roleRepository.findByName(RoleEnum.USER.getRole()).orElseThrow();
            roles.add(roleUser);
        } else {
            rolesDto.forEach(role -> {
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
        return this.userMapper.toUserDto(userSaved);
    }

    /**
     * Updated User
     * 
     * @param id User id in persistence
     * @param userDto The Data Transfer Object update the user 
     * @return retorna
     */
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        UserDto userFound = this.findUserById(id);
        userFound.setUsername(userDto.getUsername());
        userFound.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
        userFound.setEmail(userDto.getEmail());
        userFound.setFullName(userDto.getFullName());

        User userToUpdate = this.userMapper.toUser(userFound);

        User save = this.userRepository.save(userToUpdate);
        System.out.println(save);
        return this.userMapper.toUserDto(save)
                .setMessage("The user has been updated successfully!");
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
    public UserDto deleteUserById(Long id) {
        this.userRepository.delete(this.getUserById(id));
        UserDto userDto = new UserDto()
                .setMessage("The user has been successfully removed!");
        return userDto;
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

        return this.userMapper.toUserDto(this.userRepository.save(userToUpdate))
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
