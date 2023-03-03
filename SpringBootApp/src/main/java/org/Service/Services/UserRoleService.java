package org.Service.Services;


import org.Service.Entities.User;
import org.Service.Entities.UserRole;
import org.Service.Repositories.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserRoleService {
    @Autowired
    UserService userService;

    @Autowired
    UserRoleRepo userRoleRepo;

    public UserRole addUserRole(UserRole userRole) {
        User user = userService.findUserById(userRole.getUserId());
        UserRole newUserRole = new UserRole(user, user.getUserId(), userRole.getRole());
        return userRoleRepo.save(newUserRole);
    }

    public UserRole findUserRoleById(int id) {
        Optional<UserRole> userRole = userRoleRepo.findById(id);
        if (userRole.isPresent()) {
            return userRole.get();
        } else {
            throw new NoSuchElementException("User role not found");
        }
    }

    public List<User> findAllUsersExceptAdmin() {
        List<User> allUsersExceptAdmin = new ArrayList<>();
        List<User> allUsers = userService.findAllUsers();
        for (User user : allUsers) {
            UserRole userRole = findUserRoleById(user.getUserId());
            if (!userRole.getRole().equals("Admin")) {
                allUsersExceptAdmin.add(user);
            }
        }
        return allUsersExceptAdmin;
    }

    public void deleteUserRoleById(int userId) {
        UserRole userRole = findUserRoleById(userId);
        userRoleRepo.delete(userRole);
    }
}
