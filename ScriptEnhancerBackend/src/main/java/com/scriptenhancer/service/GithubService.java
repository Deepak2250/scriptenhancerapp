package com.scriptenhancer.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
public class GithubService  {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JwtService jwtService;
    
    public String GithubLogin(String code, String codeVerifier) {
        String url = "https://github.com/login/oauth/access_token";

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>(); // we are using it because the type the response we gonna get might contains multiple same keys in the same obj so we can't use the simple map
        requestParams.add("client_id", clientId);
        requestParams.add("client_secret", clientSecret);
        requestParams.add("code", code);
        requestParams.add("redirect_uri", redirectUri);
        requestParams.add("code_verifier", codeVerifier);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestParams, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
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
        String userEmailsUrl = "https://api.github.com/user/emails";

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Add the access token to the Authorization header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Prepare the HTTP entity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send GET request to GitHub's user emails endpoint
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            userEmailsUrl,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Find the primary email address
            for (Map<String, Object> email : response.getBody()) {
                if (Boolean.TRUE.equals(email.get("primary"))) {
                    return (String) email.get("email");
                }
            }
        }
        throw new RuntimeException("Failed to fetch user email: " + response.getStatusCode());
    }
}