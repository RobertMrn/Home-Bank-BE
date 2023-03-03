package org.Service.Services.JwtToken;

import org.Service.Entities.User;
import org.Service.Repositories.UserRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    UserRepo userRepo;

    private AuthenticationManager authenticationManager;

    @Lazy
    public JwtUserDetailsService(UserRepo userRepo, AuthenticationManager authenticationManager){
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            User user = userRepo.findUserByEmail(email);
            if(user !=null){
               String emailInDatabase = user.getEmail();
               String passwordInDatabase = user.getHashedPassword();
               return new org.springframework.security.core.userdetails.User(emailInDatabase,passwordInDatabase,new ArrayList<>());
            }
        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("email not found "+ email);
        }
        throw new UsernameNotFoundException("email not found "+ email);
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
