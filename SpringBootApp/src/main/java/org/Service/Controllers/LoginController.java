package org.Service.Controllers;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.DTOs.CredentialsResponse;
import org.DTOs.JwtRequest;
import org.DTOs.LoginResponse;
import org.Service.Entities.User;
import org.Service.Entities.UserRole;
import org.Service.Repositories.UserRepo;
import org.Service.Repositories.UserRoleRepo;
import org.Service.Services.JwtToken.JwtTokenGeneration;
import org.Service.Services.JwtToken.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    UserRoleRepo userRoleRepo;

    @PostMapping(value = "/authenticate")
    public LoginResponse checkCredentials(@RequestBody JwtRequest authenticationRequest) throws Exception {
        User user = userRepo.findUserByEmail(authenticationRequest.getEmail());
        UserRole userRole = userRoleRepo.findUserRoleByUser(user);
        userDetailsService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenGeneration.generateToken(new HashMap<>(), userDetails);
        String response = "," + token + "," + user.getUserId() + "," + user.getFirstName() + "," + user.getLastName() + "," +
                userRole.getRole() + "," + jwtTokenGeneration.getExpirationDateFromToken(token) + ",";
        return new LoginResponse(generateToken(new HashMap<>(), response));
    }

    public String generateToken(Map<String, Object> data, String firstName) {
        return Jwts.builder().setClaims(data).setSubject(firstName).signWith(SignatureAlgorithm.HS512, "secret").compact();
    }


}
