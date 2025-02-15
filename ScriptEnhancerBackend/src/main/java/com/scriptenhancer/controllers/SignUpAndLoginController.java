package com.scriptenhancer.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scriptenhancer.JwtConfig.JwtService;
import com.scriptenhancer.customexceptions.ValidationException;
import com.scriptenhancer.dto.UserDTO;
import com.scriptenhancer.entities.User;
import com.scriptenhancer.model.AuthRequest;
import com.scriptenhancer.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class SignUpAndLoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // Collect all errors into a list of strings
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            throw new ValidationException(errors);
        }

        User user = userService.addUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Collect all errors into a list of strings
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getRejectedValue())
                    .collect(Collectors.toList());
            throw new ValidationException(errors);
        }
        
        String jwt = jwtService.authentication(authRequest.getEmail() , authRequest.getPassword());
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + jwt) // Add the token as a Bearer token
                .header("abc", "I am a random header")
                .body("Login successful. JWT token added in the header." + jwt);

        // return ResponseEntity.ok(Map.of("token", jwt));
    }

}
