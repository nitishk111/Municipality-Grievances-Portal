package io.nitishc.grievance_portal.repository;

import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.model.Grievance;
import io.nitishc.grievance_portal.enums.Priority;
import io.nitishc.grievance_portal.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrievanceRepository extends JpaRepository<Grievance, Long> {

    public List<Grievance> findAllByUserEmail(String userEmail);

    public List<Grievance> findAllByGrievanceType(Department grievanceType);

    public List<Grievance> findAllByStatus(Status complaintType);

    public List<Grievance> findAllByPriority(Priority priorityType);
}
