package org.Service.Controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.bytebuddy.utility.RandomString;
import org.DTOs.ChangePasswordDto;
import org.DTOs.ForgotPasswordDto;
import org.DTOs.JwtRequest;
import org.DTOs.LoginResponse;
import org.Service.Entities.User;
import org.Service.Entities.UserRole;
import org.Service.Repositories.UserRepo;
import org.Service.Repositories.UserRoleRepo;
import org.Service.Services.JwtToken.JwtTokenGeneration;
import org.Service.Services.JwtToken.JwtUserDetailsService;
import org.Service.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private JavaMailSender mailSender;

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

    @Autowired
    UserService userService;

    @PostMapping(value = "/authenticate")
    public LoginResponse checkCredentials(@RequestBody JwtRequest authenticationRequest) throws Exception {
        User user = userRepo.findUserByEmail(authenticationRequest.getEmail());
        UserRole userRole = userRoleRepo.findUserRoleByUser(user);
        userDetailsService.authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenGeneration.generateToken(new HashMap<>(), userDetails);
        String response = "," + token + "," + user.getUserId() + "," + user.getFirstName() + "," + user.getLastName() + "," +
                userRole.getRole() + "," + jwtTokenGeneration.getExpirationDateFromToken(token) + ",";
        if (authenticationRequest.getPassword().equals("newUser1!")) {
            response = response + " newUser,";
        }
        return new LoginResponse(generateToken(new HashMap<>(), response));
    }

    public String generateToken(Map<String, Object> data, String firstName) {
        return Jwts.builder().setClaims(data).setSubject(firstName).signWith(SignatureAlgorithm.HS512, "secret").compact();
    }

    @PostMapping("/forgot_password")
    public ResponseEntity processForgotPassword(Model model, @RequestBody ForgotPasswordDto forgotPasswordDto) {
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, forgotPasswordDto.getEmail());
            sendEmail(forgotPasswordDto.getEmail(), token);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (NoSuchElementException | UnsupportedEncodingException | MessagingException ex) {
            return ResponseEntity.badRequest().body(400);
        }

        return ResponseEntity.ok(200);
    }

    public void sendEmail(String recipientEmail, String token) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@poliPank.com", "Poli-bank Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=" + "http://localhost:4200/newPassword?token=" + token + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @PostMapping("/newPassword")
    public ResponseEntity processResetPassword(@RequestBody ChangePasswordDto changePasswordDto, Model model) {
        String token = changePasswordDto.getToken();
        String password = changePasswordDto.getNewPassword();

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return ResponseEntity.badRequest().body(400);
        } else {
            userService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return ResponseEntity.ok(200);
    }


}
