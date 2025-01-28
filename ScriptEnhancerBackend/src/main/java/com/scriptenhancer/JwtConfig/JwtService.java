package com.scriptenhancer.JwtConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.scriptenhancer.customexceptions.WrongAuthenticationCredentials;
import com.scriptenhancer.model.AuthRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public String authentication(AuthRequest authRequest) {
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        log.info("The user has been founded : " + userDetails.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
        log.info("The Jwt Token has been successfully created : " + jwtToken);

        if (authentication.isAuthenticated()) {
            return jwtToken;
        } else {
            throw new WrongAuthenticationCredentials();
        }
    }
}
