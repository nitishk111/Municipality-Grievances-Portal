package io.nitishc.grievance_portal.service;

import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.Grievance;
import io.nitishc.grievance_portal.model.GrievanceFile;
import io.nitishc.grievance_portal.repository.GrievanceRepository;
import io.nitishc.grievance_portal.util.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@Slf4j
public class FileService {

    private final FileMapper fileMapper;
    private final GrievanceRepository grievanceRepository;

    @Autowired
    public FileService(GrievanceRepository grievanceRepository, FileMapper mapper) {
        this.fileMapper = mapper;
        this.grievanceRepository = grievanceRepository;
    }

    public String storeFile(MultipartFile file, long grievanceId, String userEmail) throws CustomException, IOException {
        Grievance grievance = grievanceRepository.findById(grievanceId).get();

        GrievanceFile grievanceFile = fileMapper.toEntity(file);
        grievanceFile.setFileName(file.getName());
        grievanceFile.setFileType(file.getContentType());
        grievanceFile.setUserEmail(userEmail);
        grievanceFile.setUploadedAt(LocalDate.now());
        grievanceFile.setGrievance(grievance);
        grievanceFile.setImage(file.getBytes());

        grievance.setLastUpdate(LocalDate.now());
        grievance.addFile(grievanceFile);
        try {
            grievanceRepository.save(grievance);
            log.info("File Saved");
            return "File saved";
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public GrievanceFile getFile(long grievanceId, long fileId) throws CustomException {
        try {
            Grievance grievance = grievanceRepository.findById(grievanceId).get();
            return grievance.getGrievanceFile().stream().filter(file->file.getId()==fileId).findFirst().get();
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CustomException(e.getMessage());
        }

    }
}
