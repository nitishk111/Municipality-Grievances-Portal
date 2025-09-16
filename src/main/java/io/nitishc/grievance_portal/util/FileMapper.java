package io.nitishc.grievance_portal.util;

import io.nitishc.grievance_portal.dto.FileDto;
import io.nitishc.grievance_portal.model.GrievanceFile;
import org.mapstruct.Mapper;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public interface FileMapper {

    public GrievanceFile toEntity(MultipartFile fileDto);

    public FileDto toDto(GrievanceFile file);
}
