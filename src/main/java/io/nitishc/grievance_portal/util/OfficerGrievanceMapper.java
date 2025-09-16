package io.nitishc.grievance_portal.util;

import io.nitishc.grievance_portal.dto.OfficerGrievanceResponse;
import io.nitishc.grievance_portal.model.Grievance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfficerGrievanceMapper {

    OfficerGrievanceResponse toDto(Grievance grievance);
}
