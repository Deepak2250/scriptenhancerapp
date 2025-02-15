package com.scriptenhancer.service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.scriptenhancer.JwtConfig.JwtService;
import com.scriptenhancer.dto.UserDTO;

@Service
public class GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JwtService jwtService;

    public String googleLogin(String code, String codeVerifier) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>(); // we are using it because the type the response we gonna get might contains multiple same keys in the same obj so we can't use the simple map
        requestParams.add("client_id", clientId);
        requestParams.add("client_secret", clientSecret);
        requestParams.add("code", code);
        requestParams.add("redirect_uri", "http://localhost:5173/oauth2/callback/google");
        requestParams.add("code_verifier", codeVerifier);
        requestParams.add("grant_type", "authorization_code"); // Added grant_type

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestParams, headers);
        
        try {
            // Exchange code for access token
            ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String accessToken = (String) response.getBody().get("access_token");
                if (accessToken != null) {
                    String email = fetchUserEmail(accessToken);
                    String generatedPassword = UUID.randomUUID().toString();

                    boolean userExistence = userService.userNotExist(email);
                    if (userExistence) {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setEmail(email);
                        userDTO.setPassword(generatedPassword);
                        userService.addUser(userDTO);
                    }

                    // Generate and return JWT
                    String jwtToken = jwtService.authentication(email, null);
                    return jwtToken;
                } else {
                    throw new RuntimeException("Access token not found in response");
                }
            } else {
                throw new RuntimeException("Failed to obtain access token");
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Error Response Body: " + e.getResponseBodyAsString());
            throw new RuntimeErrorException(null, "Error : " + e);
        }
    }

    private String fetchUserEmail(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Send GET request to Google’s user info endpoint
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.GET,
            entity,
            Map.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("email");
        } else {
            throw new RuntimeException("Failed to fetch user email");
        }
    }
}
