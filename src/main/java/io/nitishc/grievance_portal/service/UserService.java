package io.nitishc.grievance_portal.service;

import io.nitishc.grievance_portal.dto.UserResponse;
import io.nitishc.grievance_portal.dto.UserSignupRequest;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.model.User;
import io.nitishc.grievance_portal.repository.UserRepository;
import io.nitishc.grievance_portal.util.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public UserResponse userSignup(UserSignupRequest userDto) throws CustomException {
        User user = mapper.toEntity(userDto);
        User savedUser;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info("User Added");
        return mapper.toDto(savedUser);
    }

    public UserResponse userProfile(String email) throws CustomException {
        User user;
        try {
            user = userRepository.findByEmail(email);
            if (user == null) {
                throw new CustomException("No user found for email: " + email);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info("User Record with email: {} found", email);
        return mapper.toDto(user);
    }

    public UserResponse updateUser(String email, UserSignupRequest user) throws CustomException {
        User existingUser = userRepository.findByEmail(email);
        existingUser.setEmail(user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : existingUser.getEmail());
        existingUser.setPhone(user.getPhone() != null && !user.getPhone().isEmpty() ? user.getPhone() : existingUser.getPhone());
        existingUser.setFullName(user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName() : existingUser.getFullName());
        existingUser.setPassword(user.getPassword() != null && !user.getPassword().isEmpty() ? passwordEncoder.encode(user.getPassword()) : existingUser.getPassword());

        try {
            existingUser = userRepository.save(existingUser);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        log.info("Record updated Successfully");
        return mapper.toDto(existingUser);
    }

    public String deleteUser(String email) throws CustomException {
        User user = userRepository.findByEmail(email);
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(e.getMessage());
        }
        String msg = "User Deleted Successfully";
        log.info(msg);
        return msg;
    }
}
