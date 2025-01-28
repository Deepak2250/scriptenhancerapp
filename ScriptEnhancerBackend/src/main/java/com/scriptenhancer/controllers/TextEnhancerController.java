package com.scriptenhancer.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scriptenhancer.service.TextEnhancerService;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/scriptenhancer")
public class TextEnhancerController {

    @Autowired
    private TextEnhancerService textEnhancerService;

    @PostMapping("/enhance")
    public ResponseEntity<String> enhanceText(@RequestBody Map<String, String> requestBody) {
        // Extract the input text from the request body
        String inputText = requestBody.get("inputText");

        if (inputText == null || inputText.trim().isEmpty() || requestBody.isEmpty()) {
            return ResponseEntity.badRequest().body("Input text cannot be null or empty");
        }

        try {
            String enhancedText = textEnhancerService.enhanceText(inputText);
            return ResponseEntity.ok(enhancedText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error enhancing text: " + e.getMessage());
        }
    }

    @GetMapping("/testing")
    public String getMethodName() {
        return "Hi i am testing this api";
    }

}