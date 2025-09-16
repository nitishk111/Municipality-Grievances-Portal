package io.nitishc.grievance_portal.util;

import io.nitishc.grievance_portal.dto.OfficerResponse;
import io.nitishc.grievance_portal.dto.OfficerSignupRequest;
import io.nitishc.grievance_portal.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfficerMapper {

    public OfficerResponse toDto(User user);

    public User toEntity(OfficerSignupRequest request);
}
