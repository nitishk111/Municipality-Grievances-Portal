package io.nitishc.grievance_portal.controller;

import io.nitishc.grievance_portal.config.CustomUserDetails;
import io.nitishc.grievance_portal.config.JwtUtil;
import io.nitishc.grievance_portal.config.SecurityUtils;
import io.nitishc.grievance_portal.dto.EmailDto;
import io.nitishc.grievance_portal.dto.UserLoginRequest;
import io.nitishc.grievance_portal.dto.UserResponse;
import io.nitishc.grievance_portal.dto.UserSignupRequest;
import io.nitishc.grievance_portal.exception.CustomException;
import io.nitishc.grievance_portal.service.EmailService;
import io.nitishc.grievance_portal.service.UserService;
import io.nitishc.grievance_portal.util.ResponseInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseInfo<UserResponse>> addUser(@Validated @RequestBody UserSignupRequest userSignup, HttpServletRequest request) throws CustomException {
        log.info("Request Received to save new user record");
        UserResponse userResponse = userService.userSignup(userSignup);
        ResponseInfo<UserResponse> responseInfo = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), userResponse, request.getRequestURI());
        return new ResponseEntity<>(responseInfo, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseInfo<?>> userLogin(@RequestBody UserLoginRequest userDto, HttpServletRequest request) {
        try {
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(userDto.getEmail());
            ResponseInfo<?> loginSuccessful = new ResponseInfo<>(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.name(), Arrays.asList(token, role), request.getRequestURI());
//            emailService.sendEmail(new EmailDto(userDto.getEmail(), "Login Successful", "Someone logged in to your account"));
            return new ResponseEntity<>(loginSuccessful, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            ResponseInfo<String> loginSuccessful = new ResponseInfo<>(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(),"Please enter correct credentials. Or make sure you are already registered", request.getRequestURI());
            return new ResponseEntity<>(loginSuccessful, HttpStatus.UNAUTHORIZED);
        }
    }
}
