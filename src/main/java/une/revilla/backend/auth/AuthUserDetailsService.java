package une.revilla.backend.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.entity.User;
import une.revilla.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Qualifier("userDetailsService")
public class AuthUserDetailsService implements UserDetailsService {

    @Qualifier("userRepository")
    private final UserRepository userRepository;

//    @Autowired
//    public AuthUserDetailsService(@Qualifier("userRepository") UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUsername(username);
        return user.map(AuthUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user no found" + username));
    }

}
