package io.nitishc.grievance_portal.controller;

import io.nitishc.grievance_portal.config.SecurityUtils;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.GrievanceFile;
import io.nitishc.grievance_portal.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload/{grievance-id}")
    public ResponseEntity<String> addFile(@RequestParam("file") MultipartFile file, @PathVariable("grievance-id") long grievanceId) {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();
        try {
            String message = fileService.storeFile(file, grievanceId, userEmail);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload file: " + e.getMessage());
        }
    }

    @GetMapping("/{grievance-id}/{file-id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("grievance-id") long grievanceId, @PathVariable("file-id") long fileId) throws CustomException {
        GrievanceFile file = fileService.getFile(grievanceId, fileId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getImage());
    }
}
