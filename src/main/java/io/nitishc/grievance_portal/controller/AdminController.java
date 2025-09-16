package io.nitishc.grievance_portal.controller;

import io.nitishc.grievance_portal.dto.OfficerResponse;
import io.nitishc.grievance_portal.dto.OfficerSignupRequest;
import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.service.AdminOfficerService;
import io.nitishc.grievance_portal.service.OfficerService;
import io.nitishc.grievance_portal.util.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final AdminOfficerService officerService;

    @Autowired
    public AdminController(AdminOfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/add-officer")
    public ResponseEntity<ResponseInfo<OfficerResponse>> addOfficer(@Validated @RequestBody OfficerSignupRequest officerDto, HttpServletRequest request) throws CustomException {
        log.info("Request Received to save new officer record");
        OfficerResponse officerResponse = officerService.signupOfficer(officerDto);
        ResponseInfo<OfficerResponse> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(),
                officerResponse, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping("/show-officers")
    public ResponseEntity<ResponseInfo<List<OfficerResponse>>> getAllOfficers(HttpServletRequest request) {
        log.info("Request received to fetch record of all officers");
        List<OfficerResponse> officerDtos = officerService.getAllOfficersProfile();
        ResponseInfo<List<OfficerResponse>> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), officerDtos, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping("/show-officers/{department}")
    public ResponseEntity<ResponseInfo<List<OfficerResponse>>> getOfficersByDept(@PathVariable("department") Department department, HttpServletRequest request) {
        log.info("Request received to fetch record of all officers in department: {}", department);
        List<OfficerResponse> officerDtos = officerService.getOfficersProfileByDepartment(department);
        ResponseInfo<List<OfficerResponse>> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), officerDtos, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @GetMapping("/show-officer/{email}")
    public ResponseEntity<ResponseInfo<OfficerResponse>> getOfficerByEmail(@PathVariable("email") String email, HttpServletRequest request) throws CustomException {
        log.info("Request received to fetch record of via email: {}", email);
        OfficerResponse officerDto = officerService.officerProfile(email);
        ResponseInfo<OfficerResponse> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), officerDto, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/update-officer/{email}")
    public ResponseEntity<ResponseInfo<OfficerResponse>> updateOfficer(@PathVariable("email") String email, @RequestBody OfficerSignupRequest officer, HttpServletRequest request) throws CustomException {
        log.info("User with email: {}, requested to update record", email);
        OfficerResponse officerDto = officerService.updateOfficer(email, officer);
        ResponseInfo<OfficerResponse> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(),
                officerDto, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete-officer/{email}")
    public ResponseEntity<ResponseInfo<String>> deleteOfficer(@PathVariable("email") String email, HttpServletRequest request) throws CustomException {
        log.info("User with email: {}, requested to delete record", email);
        String message= officerService.deleteOfficer(email);
        ResponseInfo<String> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(),
                message, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }
}
