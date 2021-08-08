package une.revilla.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import une.revilla.backend.jwt.AuthEntryPointJwt;
import une.revilla.backend.jwt.JwtTokenVerifier;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsService")
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final JwtTokenVerifier jwtTokenVerifier;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .exceptionHandling(e -> e.authenticationEntryPoint(this.authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeRequests(authReq -> authReq.antMatchers(HttpMethod.POST, "/api/auth/**").permitAll());

        http.antMatcher("/api/**").authorizeRequests(authReq -> authReq.anyRequest().authenticated())
                .addFilterBefore(this.jwtTokenVerifier, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers",
                "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                        "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
