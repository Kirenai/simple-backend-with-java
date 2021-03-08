package une.revilla.backend.dto.mapper;

import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static List<UserDto> toUserDtoList(List<User> allUsers) {
        return allUsers
                .stream()
                .map(it -> new UserDto()
                        .setId(it.getId())
                        .setUsername(it.getUsername())
                        .setEmail(it.getEmail())
                        .setFullName(it.getFullName())
                        .setRoles(it.getRoles())
                ).collect(Collectors.toList());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setFullName(user.getFullName())
                .setRoles(user.getRoles());
    }

}
