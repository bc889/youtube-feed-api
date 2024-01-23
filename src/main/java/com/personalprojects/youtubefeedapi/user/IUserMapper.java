package com.personalprojects.youtubefeedapi.user;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    User userRequestToUser(UserRequest userRequest);

    UserDto userToUserDto(User user);
}
