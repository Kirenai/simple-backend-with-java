package une.revilla.backend.controller;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import une.revilla.backend.config.JwtConfig;
import une.revilla.backend.entity.User;
import une.revilla.backend.payload.request.LoginRequest;
import une.revilla.backend.payload.request.RegisterRequest;
import une.revilla.backend.payload.response.JwtResponse;
import une.revilla.backend.payload.response.MessageResponse;
import une.revilla.backend.service.UserService;

import javax.crypto.SecretKey;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          SecretKey secretKey,
                          JwtConfig jwtConfig, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.userService = userService;
    }

    @PostMapping("/login")
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

        User user = this.userService.findByUsername(loginRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(
                user.getId(),
                "Bearer "+token,
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (this.userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("E/R: Email is already exists!"));
        }
        this.userService.saveUser(registerRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}












