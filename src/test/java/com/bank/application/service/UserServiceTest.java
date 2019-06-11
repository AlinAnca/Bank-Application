package com.bank.application.service;

import com.bank.application.exceptions.InvalidPasswodException;
import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.exceptions.UserAlreadyLoggedException;
import com.bank.application.exceptions.UserNotFoundException;
import com.bank.application.model.Authentication;
import com.bank.application.model.DTO.UserDTO;
import com.bank.application.model.DTO.converter.UserConverter;
import com.bank.application.model.User;
import com.bank.application.repository.AuthenticationRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserService.class)
public class UserServiceTest {

    private User user;
    private UserRepository userRepository;
    private UserConverter userConverter;
    private AuthenticationRepository authenticationRepository;
    private UserService userService;

    @Before
    public void setUp() {
        user = User.builder()
                .withId(1L)
                .withUsername("test")
                .withPassword("pass")
                .build();
        userRepository = mock(UserRepository.class);
        userConverter = new UserConverter();
        authenticationRepository = mock(AuthenticationRepository.class);

        userService = new UserServiceImpl(userRepository, authenticationRepository, userConverter);
    }

    @Test
    public void login_Success() throws UserNotFoundException, UserAlreadyLoggedException, InvalidPasswodException {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.login(userConverter.convertToUserLoginDTO(user));

        assertNotNull(userDTO);
        assertEquals("test", userDTO.getUsername());
    }

    @Test
    public void login_InvalidPassword_Throws_InvalidPasswordException() throws UserNotFoundException, UserAlreadyLoggedException, InvalidPasswodException {
        User user2 = User.builder()
                .withId(2L)
                .withUsername("test")
                .withPassword("pass2")
                .build();
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user2));
        assertThrows(InvalidPasswodException.class, () -> {
            userService.login(userConverter.convertToUserLoginDTO(user));
        });
    }

    @Test
    public void login_DuplicateAuthentication_Throws_UserAlreadyLoggedException() throws UserNotFoundException, UserAlreadyLoggedException, InvalidPasswodException {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        Authentication authentication = Authentication.builder().withReference(user.getId()).build();
        when(authenticationRepository.findAuthenticationByReference(user.getId())).thenReturn(Optional.of(authentication));

        assertThrows(UserAlreadyLoggedException.class, () -> {
            userService.login(userConverter.convertToUserLoginDTO(user));
        });
    }


    @Test
    public void login_EmptyUsername_Throws_UserNotFoundException() throws UserNotFoundException, UserAlreadyLoggedException, InvalidPasswodException {
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            userService.login(userConverter.convertToUserLoginDTO(user));
        });
    }

    @Test
    public void logout_Success() throws SessionNotFoundException {
        Authentication authentication = Authentication.builder()
                .withReference(user.getId()).withToken("TestLogout").build();

        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.logout("TestLogout");
        assertNotNull(userDTO);
    }

    @Test
    public void logout_NoAuthenticationFound_Fail() throws SessionNotFoundException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> {
            userService.logout("TestLogout");
        });
    }
}
