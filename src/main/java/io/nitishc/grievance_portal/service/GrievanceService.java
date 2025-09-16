package io.nitishc.grievance_portal.service;


import io.nitishc.grievance_portal.dto.CommentRequest;
import io.nitishc.grievance_portal.dto.GrievanceRequest;
import io.nitishc.grievance_portal.dto.GrievanceResponse;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.Address;
import io.nitishc.grievance_portal.model.Comment;
import io.nitishc.grievance_portal.model.Grievance;
import io.nitishc.grievance_portal.model.GrievanceFile;
import io.nitishc.grievance_portal.repository.GrievanceRepository;
import io.nitishc.grievance_portal.util.GrievancesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GrievanceService {

    private final GrievanceRepository grievanceRepository;
    private final GrievancesMapper grievancesMapper;

    @Autowired
    public GrievanceService(GrievanceRepository grievanceRepository, GrievancesMapper mapper) {
        this.grievanceRepository = grievanceRepository;
        this.grievancesMapper = mapper;
    }

    public String saveGrievance(GrievanceRequest grievanceDto, String userEmail, MultipartFile file) throws CustomException, IOException {
        Grievance grievance = grievancesMapper.toEntity(grievanceDto);
        grievance.setUserEmail(userEmail);
        if (file != null) {
            GrievanceFile img = new GrievanceFile();
            img.setImage(file.getBytes());
            img.setFileName(file.getName());
            img.setFileType(file.getContentType());
            img.setGrievance(grievance);
            img.setUserEmail(userEmail);
            grievance.addFile(img);
        }
        Address address = grievanceDto.getAddress();
        address.setGrievance(grievance);
        grievance.setAddress(address);
        try {
            grievance = grievanceRepository.save(grievance);
        } catch (Exception e) {
            String message = "Can not save, recheck record.";
            log.warn(message, e.getMessage());
            throw new CustomException(message + e.getMessage());
        }
        String message = "Complaint registered with unique id:" + grievance.getGrievanceId();
        log.info(message);
        return message;
    }

    public List<GrievanceResponse> getGrievanceByUser(String userEmail) throws CustomException {
        List<Grievance> grievances;
        try {
            grievances = grievanceRepository.findAllByUserEmail(userEmail);
            if (grievances == null || grievances.isEmpty()) {
                String message = "No registered complain found.";
                log.warn(message);
                throw new CustomException(message);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        List<GrievanceResponse> grievanceResponses = new ArrayList<>(grievances.stream().map(grievancesMapper::toDto).toList());
        log.info("User fetched grievances");
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
        grievance.addComment(comment);
        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        log.info("Comment saved");
        return "Comment Saved";
    }

    public String updateGrievance(GrievanceRequest grievancerequest, String userEmail, long grievanceId) throws CustomException {

        Grievance grievance = grievanceRepository.findById(grievanceId).get();
        String registeredUserEmail = grievance.getUserEmail();
        if (!registeredUserEmail.equals(userEmail)) {
            String message = "No grievance record found";
            log.warn(message);
            throw new CustomException(message);
        }
        grievance.setComplaintDescription(grievancerequest.getComplaintDescription() == null || grievancerequest.getComplaintDescription().length() == 0
                ? grievance.getComplaintDescription() : grievancerequest.getComplaintDescription());

        grievance.setComplaintTitle(grievancerequest.getComplaintTitle() == null || grievancerequest.getComplaintTitle().length() == 0
                ? grievance.getComplaintTitle() : grievancerequest.getComplaintTitle());

//        grievance.setAddress(grievancerequest.getAddress() == null ? grievance.getAddress(): grievancerequest.getAddress());

        grievance.setLastUpdate(LocalDate.now());
        try {
            grievanceRepository.save(grievance);
        } catch (Exception e) {
            String message = "Can not update, recheck record.";
            log.error(message, e.getMessage());
            throw new CustomException(message + e.getMessage());
        }
        String message = "Complaint record updated";
        log.info(message);
        return message;
    }

}
