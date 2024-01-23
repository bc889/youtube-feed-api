package com.personalprojects.youtubefeedapi.user;

import java.util.List;

public interface IUserService {

    UserDto create(UserRequest userRequest);

    UserDto update(User user);

    List<UserDto> findAll();

    UserDto findById(String id);

    void delete(String id);
}
