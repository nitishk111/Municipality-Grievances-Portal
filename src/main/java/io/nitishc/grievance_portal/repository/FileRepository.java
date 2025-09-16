package io.nitishc.grievance_portal.repository;

import io.nitishc.grievance_portal.model.GrievanceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<GrievanceFile, Long> {

}
