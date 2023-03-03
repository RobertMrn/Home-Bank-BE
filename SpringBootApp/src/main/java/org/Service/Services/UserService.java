package org.Service.Services;

import org.DTOs.UserDataAndConsumerData;
import org.DTOs.UserDto;
import org.Service.Entities.CreditLoan;
import org.Service.Entities.CreditLoanConsumerData;
import org.Service.Entities.User;
import org.Service.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public User addUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getEmail(),
                userDto.getFirstName(), userDto.getLastName(), userDto.getPhoneNumber(),
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

    public List<User> findAllUsers(){
        return (List<User>) userRepo.findAll();
    }

    public User updateUserById(UserDto userDto) {
        User user = findUserById(userDto.getUserId());
        User updatedUser = new User(userDto.getUserId(), userDto.getEmail(), userDto.getFirstName(), userDto.getLastName(),
                userDto.getPhoneNumber(), userDto.getGender(), userDto.getAddress(), userDto.getNationality(),
                userDto.getBirthDate(), userDto.getPersonalUniqueCode(), user.getHashedPassword());
        return userRepo.save(updatedUser);
    }

    public void deleteUserById(int userId){
        User user = findUserById(userId);
        userRepo.delete(user);
    }

}
