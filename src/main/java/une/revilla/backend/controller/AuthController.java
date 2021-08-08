package une.revilla.backend.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.config.JwtConfig;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.dto.mapper.UserMapper;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.LoginRequest;
import une.revilla.backend.payload.response.JwtResponse;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.repository.UserRepository;
import une.revilla.backend.service.UserService;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;

@RestController
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @Qualifier(BeanIds.AUTHENTICATION_MANAGER)
    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    @Qualifier("userService")
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            log.error("Invalid credentials by user {}", loginRequest);
            throw new BadCredentialsException("Wrong user, try again " + ex.getLocalizedMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = Jwts.builder()
                .setSubject(authenticate.getName())
                .claim("authorities", authenticate.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now()
                                .plusDays(jwtConfig.getTokenExpirationAfterDays())
                        )
                )
                .signWith(this.secretKey)
                .compact();

        UserDto user = this.userService.findByUsername(loginRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(
                user.getId(),
                "Bearer " + token,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        if (this.userService.existsByEmail(userDto.getEmail())) {
            log.error("Email is already exists!");
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("E/R: Email is already exists!"));
        }

        User newUser = this.userMapper.toUser(userDto);
        User userCreated = this.userRepository.save(newUser);
        log.info("User registered successfully with his Id {}", userCreated.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully!"));
    }
}












