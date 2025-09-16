package io.nitishc.grievance_portal.controller;

import io.nitishc.grievance_portal.config.SecurityUtils;
import io.nitishc.grievance_portal.dto.CommentRequest;
import io.nitishc.grievance_portal.dto.GrievanceRequest;
import io.nitishc.grievance_portal.dto.GrievanceResponse;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.service.GrievanceService;
import io.nitishc.grievance_portal.util.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/grievances")
public class UserGrievanceController {

    private final GrievanceService grievanceService;

    @Autowired
    public UserGrievanceController(GrievanceService grievanceService) {
        this.grievanceService = grievanceService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseInfo<String>> registerGrievance(@RequestPart("grievance") GrievanceRequest grievanceRequest, @RequestPart(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws CustomException, IOException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();
        String message = grievanceService.saveGrievance(grievanceRequest, userEmail, file);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping("/show")
    public ResponseEntity<ResponseInfo<List<GrievanceResponse>>> getGrievancesByUser(HttpServletRequest request) throws CustomException {
        String userEmail= SecurityUtils.getCurrentUser().getUsername();
        List<GrievanceResponse> grievanceByUser = grievanceService.getGrievanceByUser(userEmail);
        ResponseInfo<List<GrievanceResponse>> rInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), grievanceByUser, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

    @PostMapping("/comment/{grievance-id}")
    public ResponseEntity<ResponseInfo<String>> postComment(@RequestBody CommentRequest commentRequest, @PathVariable("grievance-id") long grievanceId, HttpServletRequest request) throws CustomException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();
        String message = grievanceService.addComment(userEmail, grievanceId, commentRequest);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.ACCEPTED);
    }

    @PostMapping("/update/{grievance-id}")
    public ResponseEntity<ResponseInfo<String>> updateGrievance(@RequestBody GrievanceRequest grievancerequest, @PathVariable("grievance-id") long grievanceId, HttpServletRequest request) throws CustomException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();
        String message = grievanceService.updateGrievance(grievancerequest, userEmail, grievanceId);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }
}
