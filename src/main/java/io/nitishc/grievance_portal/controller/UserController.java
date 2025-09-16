package io.nitishc.grievance_portal.controller;

import io.nitishc.grievance_portal.config.CustomUserDetails;
import io.nitishc.grievance_portal.config.SecurityUtils;
import io.nitishc.grievance_portal.dto.UserResponse;
import io.nitishc.grievance_portal.dto.UserSignupRequest;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.service.UserService;
import io.nitishc.grievance_portal.util.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("show-profile")
    public ResponseEntity<ResponseInfo<UserResponse>> getUserProfile(HttpServletRequest request) throws CustomException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();

        log.info("Request received to fetch record of via email: {}", userEmail);
        UserResponse userDto = userService.userProfile(userEmail);
        ResponseInfo<UserResponse> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), userDto, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseInfo<UserResponse>> updateUser(@RequestBody UserSignupRequest user, HttpServletRequest request) throws CustomException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();

        log.info("User with email: {}, requested to update record", userEmail);
        UserResponse userDto = userService.updateUser(userEmail, user);
        ResponseInfo<UserResponse> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), userDto, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseInfo<String>> deleteUser(HttpServletRequest request) throws CustomException {
        String userEmail = SecurityUtils.getCurrentUser().getUsername();

        log.info("User with email: {}, requested to delete record", userEmail);
        String message = userService.deleteUser(userEmail);
        ResponseInfo<String> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), message, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }
}
