package org.Service.Services;

import org.DTOs.UserDto;
import org.Service.Entities.User;
import org.Service.Repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUser_ShouldAddUserAndReturnUser() {
        UserDto userDto = new UserDto();
        userDto.setUserId(1);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");

        User user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepo.save(any(User.class))).thenReturn(user);

        User result = userService.addUser(userDto);

        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void findUserById_ExistingId_ShouldReturnUser() {
        int userId = 1;

        User user = new User();
        user.setUserId(userId);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());

        verify(userRepo, times(1)).findById(userId);
    }

    @Test
    void findUserById_NonExistingId_ShouldThrowNoSuchElementException() {
        int userId = 1;

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.findUserById(userId));

        verify(userRepo, times(1)).findById(userId);
    }

}
