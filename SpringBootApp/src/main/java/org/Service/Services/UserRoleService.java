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
import java.util.stream.Stream;

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

    public Stream<User> findAllUsersExceptAdmin() {
        List<User> allUsers = userService.findAllUsers();
        return allUsers.stream().filter(user -> !findUserRoleById(user.getUserId()).getRole().equals("Admin"));
    }

    public void deleteUserRoleById(int userId) {
        UserRole userRole = findUserRoleById(userId);
        userRoleRepo.delete(userRole);
    }
}
