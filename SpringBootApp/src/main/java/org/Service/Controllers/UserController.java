package org.Service.Controllers;

import org.DTOs.UserDataAndConsumerData;
import org.DTOs.UserDto;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.User;
import org.Service.Entities.UserRole;
import org.Service.Repositories.UserRepo;
import org.Service.Services.CreditLoanConsumerDataService;
import org.Service.Services.UserRoleService;
import org.Service.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    CreditLoanConsumerDataService creditLoanConsumerDataService;

    @GetMapping("/hello")
    public String hello() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody UserDto userDto){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userDto.setHashedPassword(passwordEncoder.encode(userDto.getHashedPassword()));
        return userService.addUser(userDto);
    }

    @GetMapping("/findUser")
    public User findUser(@RequestParam int id){
        return userService.findUserById(id);
    }

    @GetMapping("/findUserByEmail")
    public User findUserByEmail(@RequestParam String email){
        return userRepo.findUserByEmail(email);
    }

    @PostMapping("/addUserRole")
    public UserRole addUserRole(@RequestBody UserRole userRole){
        return userRoleService.addUserRole(userRole);
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody UserDto userDto){
        return userService.updateUserById(userDto);
    }

    @GetMapping("/findAllUsers")
    public List<User> findAllUsers(){
        return userService.findAllUsers();
    }

    @GetMapping("/findAllUsersExceptAdmin")
    public Stream<User> findAllUsersExceptAdmin(){
        return userRoleService.findAllUsersExceptAdmin();
    }

    @DeleteMapping("/deleteUserById")
    public void deleteUserById(@RequestParam int userId){
        creditLoanConsumerDataService.deleteConsumerDataAndCreditLoansAndUserById(userId);
    }

    @GetMapping("/findUserDataAndConsumerDataByUserId")
    public UserDataAndConsumerData findUserDataAndConsumerDataByContractId(@RequestParam String contractId){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = contractId.split("\\.");
        String payload = new String(decoder.decode(chunks[1]));
        return creditLoanConsumerDataService.findUserDataAndConsumerDataByContractId(Integer.parseInt(payload));
    }
}
