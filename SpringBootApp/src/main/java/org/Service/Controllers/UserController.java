package org.Service.Controllers;

import org.DTOs.UserDto;
import org.Service.Entities.User;
import org.Service.Entities.UserRole;
import org.Service.Repositories.UserRepo;
import org.Service.Services.UserRoleService;
import org.Service.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/hello")
    public String hello() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody UserDto userDto){
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

}
