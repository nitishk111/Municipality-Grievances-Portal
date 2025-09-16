package io.nitishc.grievance_portal.controller;


import io.nitishc.grievance_portal.config.SecurityUtils;
import io.nitishc.grievance_portal.dto.CommentRequest;
import io.nitishc.grievance_portal.dto.OfficerGrievanceResponse;
import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.GrievanceFile;
import io.nitishc.grievance_portal.enums.Priority;
import io.nitishc.grievance_portal.enums.Status;
import io.nitishc.grievance_portal.service.AdminService;
import io.nitishc.grievance_portal.service.FileService;
import io.nitishc.grievance_portal.util.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-grievance")
public class AdminGrievanceController {

    private final AdminService adminService;

    @Autowired
    public AdminGrievanceController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("all-grievances")
    public ResponseEntity<ResponseInfo<List<OfficerGrievanceResponse>>> getGrievanceByType(HttpServletRequest request) throws CustomException {
        List<OfficerGrievanceResponse> grievanceByType = adminService.getAllGrievances();
        ResponseInfo<List<OfficerGrievanceResponse>> rInfo = new ResponseInfo<>(HttpStatus.FOUND.value(), HttpStatus.FOUND.name(),
                grievanceByType, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

    @PostMapping("/comment/{grievance-id}")
    public ResponseEntity<ResponseInfo<String>> postComment(@RequestBody CommentRequest commentRequest, @PathVariable("grievance-id") long grievanceId, HttpServletRequest request) throws CustomException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();
        String message = adminService.addComment(userEmail, grievanceId, commentRequest);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping("grievances-by-status/{status}")
    public ResponseEntity<ResponseInfo<List<OfficerGrievanceResponse>>> getGrievanceByStatus(@PathVariable("status") Status status, HttpServletRequest request) throws CustomException {
        List<OfficerGrievanceResponse> grievanceByType = adminService.getGrievanceByStatus(status);
        ResponseInfo<List<OfficerGrievanceResponse>> rInfo = new ResponseInfo<>(HttpStatus.FOUND.value(), HttpStatus.FOUND.name(), grievanceByType, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

    @GetMapping("grievances-by-priority/{priority}")
    public ResponseEntity<ResponseInfo<List<OfficerGrievanceResponse>>> getGrievanceByPriority(@PathVariable("priority") Priority priority, HttpServletRequest request) throws CustomException {
        List<OfficerGrievanceResponse> grievanceByType = adminService.getGrievanceByPriority(priority);
        ResponseInfo<List<OfficerGrievanceResponse>> rInfo = new ResponseInfo<>(HttpStatus.FOUND.value(), HttpStatus.FOUND.name(), grievanceByType, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

    @PostMapping("update-priority/{priority}/{grievance-id}")
    public ResponseEntity<ResponseInfo<String>> updateGrievancePriority(@PathVariable("priority") Priority priority, @PathVariable("grievance-id") long grievanceId, HttpServletRequest request) throws CustomException {
        String message = adminService.updatePriority(priority, grievanceId);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.FOUND.value(), HttpStatus.FOUND.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

    @PostMapping("update-status/{status}/{grievance-id}")
    public ResponseEntity<ResponseInfo<String>> updateGrievanceStatus(@PathVariable("status") Status status, @PathVariable("grievance-id") long grievanceId, HttpServletRequest request) throws CustomException {
        String message = adminService.updateStatus(status, grievanceId);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.FOUND.value(), HttpStatus.FOUND.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

    @PostMapping("update-type/{grievance-type}/{grievance-id}")
    public ResponseEntity<ResponseInfo<String>> updateGrievanceType(@PathVariable("grievance-type") Department grievanceType, @PathVariable("grievance-id") long grievanceId, HttpServletRequest request) throws CustomException {

        String message = adminService.updateGrievanceType(grievanceType, grievanceId);
        ResponseInfo<String> rInfo = new ResponseInfo<>(HttpStatus.FOUND.value(), HttpStatus.FOUND.name(), message, request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.FOUND);
    }

}
