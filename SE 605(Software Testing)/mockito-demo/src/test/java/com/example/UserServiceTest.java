package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    Hashing hashing;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        User defaultUser = new User();
        defaultUser.email = "user@example.com";
        defaultUser.pass = "hashed_secret";

        when(userRepository.getUserByEmail("user@example.com")).thenReturn(defaultUser);
        when(hashing.hashing("secret")).thenReturn("hashed_secret");
    }

    @Test
    void loginSuccess_shouldPrintLoginSuccessful() {

        String email = "user@example.com";
        String rawPassword = "secret";


        userService.login(email, rawPassword);

        }

    @Test
    void loginFail_shouldPrintUnsuccessful() {

        String email = "user@example.com";
        String rawPassword = "wrong";

        userService.login(email, rawPassword);


    }

    @Test
    void login_whenUserNotFound_shouldDoNothing() {

        when(userRepository.getUserByEmail("notfound@example.com")).thenReturn(null);

        userService.login("notfound@example.com", "anything");

    }
}
