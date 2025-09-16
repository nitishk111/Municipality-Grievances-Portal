package io.nitishc.grievance_portal.service;

import io.nitishc.grievance_portal.dto.CommentRequest;
import io.nitishc.grievance_portal.dto.OfficerGrievanceResponse;
import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.enums.Priority;
import io.nitishc.grievance_portal.enums.Status;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.*;
import io.nitishc.grievance_portal.repository.GrievanceRepository;
import io.nitishc.grievance_portal.util.OfficerGrievanceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminService {

    private final GrievanceRepository grievanceRepository;
    private final OfficerGrievanceMapper officerGrievanceMapper;
    String message = "No registered complain found.";
    String updateMessage="Complain Updated";

    @Autowired
    public AdminService(GrievanceRepository grievanceRepository, OfficerGrievanceMapper mapper) {
        this.grievanceRepository = grievanceRepository;
        this.officerGrievanceMapper = mapper;
    }

    public List<OfficerGrievanceResponse> getAllGrievances() throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAll();
            if (grievances.isEmpty()) {
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<OfficerGrievanceResponse> grievanceResponses = new ArrayList<>(grievances.stream().map(officerGrievanceMapper::toDto).toList());
        log.info("Admin fetched all grievances");
        return grievanceResponses;
    }

    public List<OfficerGrievanceResponse> getGrievanceByStatus(Status status) throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAllByStatus(status);
            if (grievances == null || grievances.isEmpty()) {
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<OfficerGrievanceResponse> grievanceResponses = new ArrayList<>(grievances.stream()
                .map(officerGrievanceMapper::toDto)
                .toList());

        log.info("Admin fetched grievances for status: {}", status);
        return grievanceResponses;
    }

    public List<OfficerGrievanceResponse> getGrievanceByPriority(Priority priority) throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAllByPriority(priority);
            if (grievances == null || grievances.isEmpty()) {
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<OfficerGrievanceResponse> grievanceResponses = new ArrayList<>(grievances.stream().map(officerGrievanceMapper::toDto).toList());

        log.info("Admin fetched grievances for status: {}", priority);
        return grievanceResponses;
    }

    public String updatePriority(Priority priority, long grievanceId) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance==null) {
            log.warn(message);
            throw new CustomException(message);
        }
        grievance.setPriority(priority);
        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info(updateMessage);
        return updateMessage;
    }

    public String updateStatus(Status status, long grievanceId) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance == null) {
            log.warn(message);
            throw new CustomException(message);
        }
        grievance.setStatus(status);
        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info(updateMessage);
        return updateMessage;
    }

    public String updateGrievanceType(Department grievanceType, long grievanceId) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance == null) {
            log.warn(message);
            throw new CustomException(message);
        }
        grievance.setGrievanceType(grievanceType);
        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info(updateMessage);
        return updateMessage;
    }

    public String addComment(String userEmail, long grievanceId, CommentRequest commentRequest) throws CustomException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        if (grievance == null) {
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
}
