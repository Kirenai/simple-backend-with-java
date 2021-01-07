package une.revilla.backend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import une.revilla.backend.config.JwtConfig;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

//First filter
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JwtConfig jwtConfig,
                                                      SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    //Autenticando credenciales
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(
                            request.getInputStream(),
                            UsernameAndPasswordAuthenticationRequest.class
                    );

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );

            return this.authenticationManager.authenticate(authentication);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //Si hay buena autenticación genera el token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(authResult.getName())   //username
                .claim("authorities", authResult.getAuthorities())  //authorities
                .setIssuedAt(new Date())    //start token
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(this.jwtConfig.getTokenExpirationAfterDays())))    //expire token
                .signWith(this.secretKey)   //my secret key in bytes[]
                .compact(); //build JWS

        response.addHeader(
                this.jwtConfig.getAuthorizationHeader(),
                this.jwtConfig.getTokenPrefix() + token
        );  //add header "Authorization"
    }

}
