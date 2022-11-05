package org.Service.Controllers;


import org.DTOs.JwtRequest;
import org.DTOs.CredentialsResponse;
import org.DTOs.LoginResponse;
import org.Service.Entities.User;
import org.Service.Repositories.UserRepo;
import org.Service.Services.JwtToken.JwtTokenGeneration;
import org.Service.Services.JwtToken.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenGeneration jwtTokenGeneration;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    UserRepo userRepo;

    @PostMapping(value = "/authenticate")
    public LoginResponse checkCredentials(@RequestBody JwtRequest authenticationRequest) throws Exception {
        User user = userRepo.findUserByEmail(authenticationRequest.getEmail());
        userDetailsService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenGeneration.generateToken(new HashMap<>(), userDetails);
        BCryptPasswordEncoder objectEncoder = new BCryptPasswordEncoder();
        CredentialsResponse credentialsResponse = new CredentialsResponse(user.getFirstName(), user.getLastName(),token);
        return new LoginResponse(token);
    }


}
