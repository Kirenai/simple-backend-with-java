package une.revilla.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import une.revilla.backend.config.JwtConfig;
import une.revilla.backend.dto.RoleDto;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.dto.mapper.UserMapper;
import une.revilla.backend.jwt.AuthEntryPointJwt;
import une.revilla.backend.repository.RoleRepository;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.UserService;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;
    
    @MockBean
    private JwtConfig jwtConfig;

    @MockBean
    private SecretKey secretKey;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    private UserDto userDto;
    private UserDto userDtoSaved;

    @BeforeEach
    public void setUp() {
        RoleDto roleDto = RoleDto.builder().id(1L).name("ROLE_USER").build();
        System.out.println(roleDto);

        userDto = UserDto.builder()
            .username("Lucas")
            .password("Lucas")
            .email("lucas@mail.com")
            .fullName("Lucas Vargas")
            .build();
        System.out.println(userDto);
        
        userDtoSaved = UserDto.builder()
            .id(1L)
            .username("Lucas")
            .password("Lucas")
            .email("lucas@mail.com")
            .fullName("Lucas Vargas")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .tasks(Collections.emptyList())
            .roles(Set.of(roleDto))
            .build();
        System.out.println(userDtoSaved);
    }

    @Test
    @DisplayName("Get: /api/user/{id}, getUser")
    @Disabled
    public void testGetUser() throws Exception {
        Mockito.when(this.userService.findUserById(1L))
            .thenReturn(userDto);

        mockMvc.perform(get("/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST: /api/user, save and get DataUserDto")
    @Disabled
    public void testSave() throws JsonProcessingException, Exception {
        Mockito.when(this.userService.saveUser(userDto))
            .thenReturn(userDtoSaved);

        String uri = "/";

        mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(userDto)))
            .andExpect(status().isCreated());
    }
}
