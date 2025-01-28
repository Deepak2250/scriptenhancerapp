package com.scriptenhancer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TextEnhancerService {

    private static final String HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/gpt2";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${huggingface.api.token}")
    private String huggingFaceApiToken;

    private final ObjectMapper objectMapper;

    public TextEnhancerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String enhanceText(String inputText) {
        if (inputText == null || inputText.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be null or empty");
        }

        // Set up the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + huggingFaceApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body with the text to enhance
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", inputText); // Provide the text to be enhanced
        requestBody.put("parameters", createParameters()); // Set optional parameters

        // Prepare the entity to send in the POST request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Send POST request to Hugging Face API
            ResponseEntity<String> response = restTemplate.exchange(
                    HUGGINGFACE_API_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Check for a successful response
            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse the response to get the enhanced text
                return extractEnhancedText(response.getBody());
            } else {
                throw new RuntimeException("Failed to enhance text: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle errors like 4xx or 5xx status codes
            throw new RuntimeException("Error while communicating with HuggingFace API: " + e.getMessage());
        } catch (Exception e) {
            // Handle other unforeseen exceptions
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    private Map<String, Object> createParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 0.6);
        parameters.put("max_tokens", 50);
        parameters.put("top_p", 0.9);
        return parameters;
    }

    private String extractEnhancedText(String response) {
        try {
            // Parse the response JSON to extract the enhanced text
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode generatedTextNode = rootNode.get(0); // Assuming the response is an array
            return generatedTextNode.get("generated_text").asText(); // Extract the generated_text field
        } catch (Exception e) {
            throw new RuntimeException("Error parsing response from Hugging Face API: " + e.getMessage());
        }
    }
}
