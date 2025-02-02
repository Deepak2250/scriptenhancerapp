package com.scriptenhancer.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scriptenhancer.entities.InputAndOutput;
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
           textEnhancerService.saveInputAndOutPut(inputText, enhancedText);
            return ResponseEntity.ok(enhancedText);
        } 
        catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error enhancing text: " + e.getMessage());
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<InputAndOutput>> getHistory() {
         List<InputAndOutput> inputAndOutputs = textEnhancerService.getHistory();
         return ResponseEntity.ok().body(inputAndOutputs);
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteInputAndOutput(@RequestParam long id){
    	Boolean response = textEnhancerService.deleteInputAndOutput(id);
    	return ResponseEntity.ok().body("The" + id + " is got deleted ? " +response);
    }
    

}