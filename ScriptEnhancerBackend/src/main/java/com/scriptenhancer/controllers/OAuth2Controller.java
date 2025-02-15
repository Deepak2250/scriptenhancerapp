package com.scriptenhancer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scriptenhancer.model.OAuth2Request;
import com.scriptenhancer.service.GithubService;
import com.scriptenhancer.service.GoogleService;

@RestController
@RequestMapping("/api/auth")
public class OAuth2Controller {
	
	@Autowired
	private GithubService auth2Service;
	
	@Autowired
	private GoogleService googleService;

    @PostMapping("/github")
    public ResponseEntity<String> handleGitHubCallback(@RequestBody OAuth2Request oAuth2Request) {
        
    	String jwt = auth2Service.GithubLogin(oAuth2Request.getCode(), oAuth2Request.getCodeVerifier());
    	 return ResponseEntity.status(HttpStatus.OK)
                 .header("Authorization", "Bearer " + jwt) // Add the token as a Bearer token
                 .body("Login successful. JWT token added in the header." + jwt);

    }

    
    @PostMapping("/google")
    public ResponseEntity<String> handleGoogleCallback(@RequestBody OAuth2Request oAuth2Request) {
        
    	String jwt = googleService.googleLogin(oAuth2Request.getCode(), oAuth2Request.getCodeVerifier());
    	 return ResponseEntity.status(HttpStatus.OK)
                 .header("Authorization", "Bearer " + jwt) // Add the token as a Bearer token
                 .body("Login successful. JWT token added in the header." + jwt);

    }
    
}