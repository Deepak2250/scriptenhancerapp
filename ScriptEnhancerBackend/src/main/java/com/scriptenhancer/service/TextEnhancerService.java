package com.scriptenhancer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptenhancer.customexceptions.NoInputAndOutputFound;
import com.scriptenhancer.entities.InputAndOutput;
import com.scriptenhancer.entities.User;
import com.scriptenhancer.model.CustomUserDetails;
import com.scriptenhancer.repository.InputAndOutputRepository;
import com.scriptenhancer.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TextEnhancerService {

    private static final String HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/gpt2";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${huggingface.api.token}")
    private String huggingFaceApiToken;

    @Autowired 
    private InputAndOutputRepository inputAndOutputRepository;
    @Autowired
    private UserRepository userRepository;
    
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
        parameters.put("temperature", 0.9);           // Creativity in the text generation
        parameters.put("max_length", 1500);           // Allow more text to be generated
        parameters.put("top_p", 0.9);                 // Nucleus sampling
        parameters.put("top_k", 50);                  // Limit to top 50 tokens
        parameters.put("repetition_penalty", 1.2);    // Avoid repetitive outputs
        parameters.put("length_penalty", 1.0);        // Balance the output length
        parameters.put("num_return_sequences", 1);    // Return 1 sequence of text
        parameters.put("do_sample", true);            // Enable sampling for creativity
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

    @Transactional
    public void saveInputAndOutPut(String input , String output) {
    	
    	
    	  CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    User currentUser = customUserDetails.getUser();
    	   
    	    // Create the InputAndOutput object
    	    InputAndOutput inputAndOutput = new InputAndOutput();
    	    inputAndOutput.setInputs(input);
    	    inputAndOutput.setOutputs(output);

    	    inputAndOutput.setUser(List.of(currentUser));
    	    currentUser.getInputAndOutputs().add(inputAndOutput);
    	    userRepository.save(currentUser);
    	
   
    }
    
    @Transactional
    public boolean deleteInputAndOutput(long id) {
    	log.info("The id is : " +id);
    	
    	  CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    Optional<User> currentUser = Optional.ofNullable(customUserDetails.getUser());
    	    		
    	     InputAndOutput inputAndOutput = inputAndOutputRepository.findById(id)
    	    		 .orElseThrow(() -> new NoInputAndOutputFound("This input or output are not present in the db "+ id));
    	   
    	     if (currentUser.isPresent()) {
    	    	 currentUser = userRepository.findByEmail(currentUser.get().getEmail());   //found the user who is in database or not either throw an exception hibernate saves the persistance of the user in its cache and when we compares it with the list of users then it said true
				 if (inputAndOutput.getUser().contains(currentUser.get())) {
						currentUser.get().getInputAndOutputs().remove(inputAndOutput);
						inputAndOutput.getUser().remove(currentUser.get());
						userRepository.save(currentUser.get());
						return true;
					} 
				 else {
					 throw new NoInputAndOutputFound("You Dont have the permission to delete this id : " +id);
				 }
			};   
			
    	 
    	throw new UsernameNotFoundException("No User founded check your credentials or signup first");
    	
   
    }
    
    
    public List<InputAndOutput>getHistory(){
    	
    	CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	User user = customUserDetails.getUser();
    	
    	List<InputAndOutput> inputAndOutputs = inputAndOutputRepository.findAllByUser(user);
    	
    	if (inputAndOutputs.isEmpty()) {
			throw new NoInputAndOutputFound("No History founded");
		}
    	
    	return inputAndOutputs;
    	  
    }
}
