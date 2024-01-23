package com.personalprojects.youtubefeedapi.user;

import com.personalprojects.youtubefeedapi.error.NotFoundException;
import com.personalprojects.youtubefeedapi.error.enums.NotFoundErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IUserMapper userMapper;

    @Override
    public UserDto create(UserRequest userRequest) {

        User userToCreate = userMapper.userRequestToUser(userRequest);

        User createdUser = userRepository.save(userToCreate);

        return userMapper.userToUserDto(createdUser);
    }

    @Override
    public UserDto update(User user) {

        User updatedUser = userRepository.save(user);

        return userMapper.userToUserDto(updatedUser);
    }

    @Override
    public List<UserDto> findAll() {

        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(String id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {

            throw new NotFoundException(NotFoundErrorCode.USER_ID_NOT_FOUND);
        }

        return userMapper.userToUserDto(optionalUser.get());
    }

    @Override
    public void delete(String id) {

        userRepository.deleteById(id);
    }
}
