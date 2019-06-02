package com.bank.application.service.impl;

import com.bank.application.exceptions.*;
import com.bank.application.model.Authentication;
import com.bank.application.model.DTO.UserDTO;
import com.bank.application.model.DTO.UserLoginDTO;
import com.bank.application.model.DTO.converter.UserConverter;
import com.bank.application.model.User;
import com.bank.application.repository.AuthenticationRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private AuthenticationRepository authenticationRepository;
    private UserConverter userConverter;
    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthenticationRepository authenticationRepository, UserConverter userConverter) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDTO save(User user) throws UsernameAlreadyExistsException {
        Optional<User> user2 = userRepository.findUserByUsername(user.getUsername());
        if (!user2.isPresent()) {
            return userConverter.convertToUserDTO(userRepository.save(user));
        } else
            throw new UsernameAlreadyExistsException("Username already exists. Please choose another one.");
    }

    @Override
    public UserDTO findUserByUsername(String username) {
        return userConverter.convertToUserDTO(userRepository.findUserByUsername(username).get());
    }

    @Override
    public UserDTO deleteUser(String username) {
        return userConverter.convertToUserDTO(userRepository.deleteUserByUsername(username));
    }

    @Override
    public UserDTO login(UserLoginDTO userLoginDTO) throws InvalidPasswodException, UserNotFoundException, UserAlreadyLoggedException {
        Optional<User> user = userRepository.findUserByUsername(userLoginDTO.getUsername());
        if (user.isPresent()) {
            if (!authenticationRepository.findAuthenticationByReference(user.get().getId()).isPresent()) {
                LOGGER.trace("Username or password is incorrect!");
                if (user.get().getPassword().equals(userLoginDTO.getPassword())) {
                    String generatedToken = "";
                    Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(generatedToken);
                    while (generatedToken.isEmpty() || authentication.isPresent()) {
                        generatedToken = RandomStringUtils.randomAlphanumeric(20);
                    }
                    Authentication newAuthentication = Authentication
                            .builder()
                            .withReference(user.get().getId())
                            .withToken(generatedToken)
                            .build();
                    authenticationRepository.save(newAuthentication);
                    return userConverter.convertToUserDTO(user.get());
                } else
                    throw new InvalidPasswodException();
            } else
                throw new UserAlreadyLoggedException("User '" + userLoginDTO.getUsername() + "' already logged!");
        }
        throw new UserNotFoundException("Username '" + userLoginDTO.getUsername() + "' not found!");
    }

    @Override
    public UserDTO logout(String token) throws SessionNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            authenticationRepository.deleteAuthenticationByToken(token);
            return userConverter.convertToUserDTO(userRepository.findUserById(authentication.get().getReference()).get());
        }
        throw new SessionNotFoundException("User not logged!");
    }
}