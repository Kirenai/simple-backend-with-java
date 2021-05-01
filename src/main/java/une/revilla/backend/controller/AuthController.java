package une.revilla.backend.controller;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.config.JwtConfig;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.payload.request.LoginRequest;
import une.revilla.backend.payload.response.JwtResponse;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.service.UserService;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authenticate;
        try {
             authenticate = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            logger.info("Invalid credentials by user {}", loginRequest);
            throw new BadCredentialsException("Wrong user, try again");
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = Jwts.builder()
                .setSubject(authenticate.getName())
                .claim("authorities", authenticate.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(this.secretKey)
                .compact();

        UserDto user = this.userService.findByUsername(loginRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(
                user.getId(),
                "Bearer "+token,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles())
        );
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        if (this.userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("E/R: Email is already exists!"));
        }
        this.userService.saveUser(userDto);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}












