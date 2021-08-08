package une.revilla.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.dto.mapper.UserMapper;
import une.revilla.backend.repository.RoleRepository;
import une.revilla.backend.repository.TaskRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.imp.UserServiceImp;
/**
 * UserServiceTest
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserServiceImp userService;

    @Mock
    private UserMapper userMapper;

    @MockBean
    private ObjectMapper objectMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Pageable pageable;

    private List<UserDto> users;
    private UserDto firstUser;
    private UserDto secondUser;
    private UserDto thirdUser;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        pageable = PageRequest.of(0, 2, Sort.by("id").ascending());

        RoleDto userRole = RoleDto.builder()
                .name("ROLE_USER")
                .message("")
                .build();

        firstUser = UserDto.builder()
                .id(1L)
                .username("user_one")
                .password("user_one")
                .email("user_one@mail.com")
                .fullName("User One")
                .tasks(Collections.emptyList())
                .roles(Set.of(userRole))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .message("")
                .build();

        secondUser = UserDto.builder()
                .id(2L)
                .username("user_two")
                .password("user_two")
                .email("user_two@mail.com")
                .fullName("User Two")
                .tasks(Collections.emptyList())
                .roles(Set.of(userRole))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .message("")
                .build();

        users.add(firstUser);
        users.add(secondUser);

        thirdUser = UserDto.builder()
                .id(3L)
                .username("user_three")
                .password("user_three")
                .email("user_three@mail.com")
                .fullName("User Three")
                .tasks(Collections.emptyList())
                .roles(Set.of(userRole))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .message("")
                .build();
    }

    @Test
    @DisplayName("Find all users Test")
    @Disabled
    public void testWhenFindAllUsersThenAllUsersPaginateShouldFound() {
        Mockito.when(this.userService.findAllUsers(Mockito.any(Pageable.class)))
                .thenReturn(this.users);

        List<UserDto> findAllUsers = this.userService.findAllUsers(pageable);

        assertEquals(findAllUsers.size(), 2);
    }

    @Test
    @DisplayName("Find one user test")
    @Disabled
    public void whenFindOneUserByIdThenOneUserDtoShouldFound() {
        Long userId = 3L;

        Mockito.when(this.userService.findUserById(userId))
                .thenReturn(this.thirdUser);

        UserDto userFound = this.userService.findUserById(userId);

        assertEquals(userFound.getId(), userId);
        assertEquals(userFound.getUsername(), "user_three");
        assertEquals(userFound.getEmail(), "user_three@mail.com");
    }
}
