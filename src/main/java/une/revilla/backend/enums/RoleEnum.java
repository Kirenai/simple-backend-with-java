package une.revilla.backend.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    USER("ROLE_USER"),
    MODERATOR("ROLE_MODERATOR"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }


}
