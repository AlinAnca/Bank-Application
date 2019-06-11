package com.bank.application.controller;

import com.bank.application.exceptions.*;
import com.bank.application.model.DTO.UserDTO;
import com.bank.application.model.DTO.UserLoginDTO;
import com.bank.application.model.User;
import com.bank.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{username}")
    public UserDTO findUserByUsername(@PathVariable("username") String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> save(@Valid @RequestBody User user) throws UsernameAlreadyExistsException {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{username}")
    public UserDTO deleteUser(@PathVariable("username") String username) {
        return userService.deleteUser(username);
    }

    @GetMapping(value = "/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserLoginDTO userLoginDTO) throws InvalidPasswodException, UserNotFoundException, UserAlreadyLoggedException {
        return new ResponseEntity<>(userService.login(userLoginDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/logout")
    public UserDTO logout(@RequestParam String token) throws SessionNotFoundException {
        return userService.logout(token);
    }

}
