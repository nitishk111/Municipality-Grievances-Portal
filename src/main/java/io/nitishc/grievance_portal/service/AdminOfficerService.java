package io.nitishc.grievance_portal.service;

import io.nitishc.grievance_portal.dto.OfficerResponse;
import io.nitishc.grievance_portal.dto.OfficerSignupRequest;
import io.nitishc.grievance_portal.enums.Department;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.User;
import io.nitishc.grievance_portal.repository.UserRepository;
import io.nitishc.grievance_portal.util.OfficerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminOfficerService {

    private final UserRepository userRepository;
    private final OfficerMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminOfficerService(UserRepository userRepository, OfficerMapper mapper, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public OfficerResponse signupOfficer(OfficerSignupRequest officerDto) throws CustomException {
        User officer = mapper.toEntity(officerDto);
        officer.setPassword(passwordEncoder.encode(officer.getPassword()));
        User savedUser;
        try {
            savedUser = userRepository.save(officer);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info("Officer record added");
        return mapper.toDto(savedUser);
    }

    public OfficerResponse officerProfile(String email) throws CustomException {
        User officer;
        try {
            officer = userRepository.findByEmail(email);
            if (officer == null) {
                log.warn("No Officer Record found with email: {} ", email);
                throw new CustomException("Officer not found with email: " + email);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info("User Record with email: {} found", email);
        return mapper.toDto(officer);
    }

    public OfficerResponse updateOfficer(String email, OfficerSignupRequest officer) throws CustomException {
        User existingOfficer;
        try {
            existingOfficer = userRepository.findByEmail(email);
            if (existingOfficer == null) {
                log.warn("No Officer Records with email: {} found", email);
                throw new CustomException("Officer not found with email: " + email);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        existingOfficer.setEmail(officer.getEmail() != null && !officer.getEmail().isEmpty() ? officer.getEmail() : existingOfficer.getEmail());
        existingOfficer.setPhone(officer.getPhone() != null && !officer.getPhone().isEmpty() ? officer.getPhone() : existingOfficer.getPhone());
        existingOfficer.setFullName(officer.getFullName() != null && !officer.getFullName().isEmpty() ? officer.getFullName() : existingOfficer.getFullName());
        existingOfficer.setPassword(officer.getPassword() != null && !officer.getPassword().isEmpty() ? passwordEncoder.encode(officer.getPassword()) : existingOfficer.getPassword());
        existingOfficer.setRole(officer.getRole() != null ? officer.getRole() : existingOfficer.getRole());
        existingOfficer.setDepartment(officer.getDepartment() != null ? officer.getDepartment() : existingOfficer.getDepartment());

        try {
            existingOfficer = userRepository.save(existingOfficer);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info("Record updated Successfully");
        return mapper.toDto(existingOfficer);
    }

    public String deleteOfficer(String email) throws CustomException {
        User officer = userRepository.findByEmail(email);
        try {
            userRepository.delete(officer);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        String msg = "officer record deleted Successfully";
        log.info(msg);
        return msg;
    }

    public List<OfficerResponse> getAllOfficersProfile() {
        return userRepository.findAll().stream().filter(user -> user.getRole().toString().equals("ROLE_OFFICER")).map(mapper::toDto).toList();
    }

    public List<OfficerResponse> getOfficersProfileByDepartment(Department department) {
        return userRepository.findAll().stream().filter(user -> user.getRole().toString().equals("ROLE_OFFICER") && user.getDepartment().equals(department)).map(mapper::toDto).toList();
    }
}
