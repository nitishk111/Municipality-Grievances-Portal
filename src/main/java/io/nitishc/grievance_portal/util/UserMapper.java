package io.nitishc.grievance_portal.util;


import io.nitishc.grievance_portal.dto.UserResponse;
import io.nitishc.grievance_portal.dto.UserSignupRequest;
import io.nitishc.grievance_portal.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDto(User user);

    User toEntity(UserSignupRequest request);
}
