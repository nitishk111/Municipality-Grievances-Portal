package io.nitishc.grievance_portal.service;

import io.nitishc.grievance_portal.dto.CommentRequest;
import io.nitishc.grievance_portal.dto.OfficerGrievanceResponse;
import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.enums.Priority;
import io.nitishc.grievance_portal.enums.Status;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.Comment;
import io.nitishc.grievance_portal.model.Grievance;
import io.nitishc.grievance_portal.repository.GrievanceRepository;
import io.nitishc.grievance_portal.util.OfficerGrievanceMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OfficerService {

    private final GrievanceRepository grievanceRepository;
    private final OfficerGrievanceMapper officerGrievanceMapper;

    @Autowired
    public OfficerService(OfficerGrievanceMapper mapper, GrievanceRepository grievanceRepository) {
        this.officerGrievanceMapper = mapper;
        this.grievanceRepository = grievanceRepository;

    }

    public List<OfficerGrievanceResponse> getGrievanceByType(Department grievanceType) throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAllByGrievanceType(grievanceType);
            if (grievances == null || grievances.isEmpty()) {
                String message = "No registered complain found.";
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<OfficerGrievanceResponse> grievanceResponses = new ArrayList<>(grievances.stream().map(officerGrievanceMapper::toDto).toList());
        log.info("Officer fetched grievances for department: {}", grievanceType);
        return grievanceResponses;
    }

    public List<OfficerGrievanceResponse> getGrievanceByStatus(Status status, Department grievanceType) throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAllByStatus(status);
            if (grievances == null || grievances.isEmpty()) {
                String message = "No registered complain found.";
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<OfficerGrievanceResponse> grievanceResponses = new ArrayList<>(grievances.stream().
                filter(g->g.getGrievanceType().equals(grievanceType)).map(officerGrievanceMapper::toDto).toList());
        log.info("Officer fetched grievances for status: {}", status);
        return grievanceResponses;
    }

    public List<OfficerGrievanceResponse> getGrievanceByPriority(Priority priority, Department grievanceType) throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAllByPriority(priority);
            if (grievances == null || grievances.isEmpty()) {
                String message = "No registered complain found.";
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<OfficerGrievanceResponse> grievanceResponses = new ArrayList<>(
                grievances.stream().filter(g->g.getGrievanceType().equals(grievanceType))
                        .map(officerGrievanceMapper::toDto).toList());
        log.info("Officer fetched grievances for priority: {}", priority);
        return grievanceResponses;
    }

    public String addComment(String userEmail, long grievanceId, CommentRequest commentRequest) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance == null) {
            String message = "No complaint found with grievance id: " + grievanceId;
            log.warn(message);
            throw new CustomException(message);
        }
        Comment comment = new Comment(userEmail, grievance, commentRequest);
        comment.setGrievance(grievance);
        grievance.setLastUpdate(LocalDate.now());
        grievance.addComment(comment);
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        log.info("Comment saved");
        return "Comment Saved";
    }

    public String updatePriority(Priority priority, long grievanceId, Department grievanceType) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance == null || !grievance.getGrievanceType().equals(grievanceType)) {
            String message = "No grievance record found";
            log.warn(message);
            throw new CustomException(message);
        }
        grievance.setPriority(priority);
        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            String message = "Can not update, recheck record.";
            log.warn(message, e.getMessage());
            throw new CustomException(message + e.getMessage());
        }
        String message = "Complaint Priority updated";
        log.info(message);
        return message;
    }

    public String updateStatus(Status status, long grievanceId, Department grievanceType) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance == null || !grievance.getGrievanceType().equals(grievanceType)) {
            String message = "No grievance record found";
            log.warn(message);
            throw new CustomException(message);
        }
        grievance.setStatus(status);
        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            String message = "Can not update, recheck record.";
            log.error(message, e.getMessage());
            throw new CustomException(message + e.getMessage());
        }
        String message = "Complaint Status updated";
        log.info(message);
        return message;
    }
}
