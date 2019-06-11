package com.bank.application.service;

import com.bank.application.exceptions.*;
import com.bank.application.model.DTO.UserDTO;
import com.bank.application.model.DTO.UserLoginDTO;
import com.bank.application.model.User;

public interface UserService {

    UserDTO save(User user) throws UsernameAlreadyExistsException;

    UserDTO findUserByUsername(String username);

    UserDTO deleteUser(String username);

    UserDTO login(UserLoginDTO userLoginDTO) throws InvalidPasswodException, UserNotFoundException, UserAlreadyLoggedException;

    UserDTO logout(String token) throws SessionNotFoundException;
}
