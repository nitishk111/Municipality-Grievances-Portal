package io.nitishc.grievance_portal.exception;

import io.nitishc.grievance_portal.util.ErrorInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleGlobalException(MethodArgumentNotValidException exception, HttpServletRequest request){
        Map<String, String> errors= new HashMap<>();
        for(FieldError error: exception.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorInfo rInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), errors.toString(),
                request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorInfo> customException(Exception e, HttpServletRequest request) {
        ErrorInfo rInfo = new ErrorInfo(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.name(), e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(rInfo, HttpStatus.CONFLICT);
    }

}
