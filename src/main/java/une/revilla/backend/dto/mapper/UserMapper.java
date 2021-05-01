package une.revilla.backend.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import une.revilla.backend.dto.UserDto;
import une.revilla.backend.entity.User;

@Component
@Scope("singleton")
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public List<UserDto> toUserDtoList(List<User> allUsers) {
        return allUsers
                .stream()
                .map(user -> this.modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserDto toUserDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }

    public User toUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

}
