package org.Service.Services;

import org.DTOs.UserDto;
import org.Service.Entities.User;
import org.Service.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User addUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getEmail(),
                userDto.getFirstName(), userDto.getLastName(), userDto.getPhoneNumber(), userDto.getResetPasswordToken(),
                userDto.getGender(), userDto.getAddress(), userDto.getNationality(),
                userDto.getBirthDate(), userDto.getPersonalUniqueCode(), userDto.getHashedPassword());
        userRepo.save(user);
        return user;
    }

    public User findUserById(int id) {
        Optional<User> user = userRepo.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NoSuchElementException("User not found");
        }
    }

    public List<User> findAllUsers() {
        return (List<User>) userRepo.findAll();
    }

    public User updateUserById(UserDto userDto) {
        User user = findUserById(userDto.getUserId());
        User updatedUser = new User(userDto.getUserId(), userDto.getEmail(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getPhoneNumber(), userDto.getResetPasswordToken(), userDto.getGender(), userDto.getAddress(), userDto.getNationality(),
                userDto.getBirthDate(), userDto.getPersonalUniqueCode(), user.getHashedPassword());
        return userRepo.save(updatedUser);
    }

    public void deleteUserById(int userId) {
        User user = findUserById(userId);
        userRepo.delete(user);
    }

    public void updateResetPasswordToken(String token, String email) {
        User user = userRepo.findUserByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepo.save(user);
        } else {
            throw new NoSuchElementException("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setHashedPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepo.save(user);
    }

}
