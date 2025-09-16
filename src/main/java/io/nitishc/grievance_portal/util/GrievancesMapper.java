package io.nitishc.grievance_portal.util;

import io.nitishc.grievance_portal.dto.GrievanceRequest;
import io.nitishc.grievance_portal.dto.GrievanceResponse;
import io.nitishc.grievance_portal.model.Grievance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GrievancesMapper {

    public GrievanceResponse toDto(Grievance grievance);

    public Grievance toEntity(GrievanceRequest grievanceRequest);
}
