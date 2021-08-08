package une.revilla.backend.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Data
@Slf4j
@NoArgsConstructor
public class JwtConfig {
    @Value("${application.jwt.secretKey}")
    private String secretKey;
    @Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${application.jwt.tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public Claims getJwtBody(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public Set<SimpleGrantedAuthority> getJwtAuthorities(Claims body) {
        var authorities = (List<Map<String, String>>) body.get("authorities");

        return authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(this.secretKey.getBytes())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
