package com.scriptenhancer.globalexception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.scriptenhancer.customexceptions.AdminDeletionIsNotAllowed;
import com.scriptenhancer.customexceptions.AlreadyUsedEmail;
import com.scriptenhancer.customexceptions.ImageNotSaved;
import com.scriptenhancer.customexceptions.NoInputAndOutputFound;
import com.scriptenhancer.customexceptions.UsersNotFound;
import com.scriptenhancer.customexceptions.ValidationException;
import com.scriptenhancer.customexceptions.WrongAuthenticationCredentials;

@RestControllerAdvice
public class RestGlobaladvice {

    @ExceptionHandler(AlreadyUsedEmail.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(AlreadyUsedEmail ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("Error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMap); // Or use an appropriate status code
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation errors occurred");
        response.put("errors", ex.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(WrongAuthenticationCredentials.class)
    public ResponseEntity<Map<String, Object>> handleWrongAuthenticationCredentials(WrongAuthenticationCredentials ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(UsersNotFound.class)
    public ResponseEntity<Map<String, Object>> handleUsersNotFound(UsersNotFound ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(NoInputAndOutputFound.class)
    public ResponseEntity<Map<String, Object>> handleInputAndOutput(NoInputAndOutputFound ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler(AdminDeletionIsNotAllowed.class)
    public ResponseEntity<Map<String, Object>> handleAdminDeletionIsNotAllowed(AdminDeletionIsNotAllowed ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler(ImageNotSaved.class)
    public ResponseEntity<Map<String, Object>> handleImageNotSaved(ImageNotSaved ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
