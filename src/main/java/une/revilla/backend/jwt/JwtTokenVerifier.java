package une.revilla.backend.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import une.revilla.backend.config.JwtConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Second filter
@Slf4j
@Component
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenVerifier(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = this.getToken(request);
            if (this.jwtConfig.validateJwtToken(token)) {
                Claims body = this.jwtConfig.getJwtBody(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        body.getSubject(),
                        null,
                        this.jwtConfig.getJwtAuthorities(body)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Can't set user authentication: {}", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token.startsWith("Bearer ") && StringUtils.hasText(token)) {
            return token.substring(7, token.length());
        }
        return null;
    }
}
