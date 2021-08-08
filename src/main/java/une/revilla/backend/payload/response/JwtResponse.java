package une.revilla.backend.payload.response;

import lombok.*;
import une.revilla.backend.dto.RoleDto;

import java.util.Collection;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    @NonNull
    private Long id;
    @NonNull
    private String token;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String fullName;
    @NonNull
    private Collection<RoleDto> roles;
}
