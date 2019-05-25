package com.bank.application.controller;

import com.bank.application.exceptions.*;
import com.bank.application.model.DTO.UserDTO;
import com.bank.application.model.DTO.UserLoginDTO;
import com.bank.application.model.User;
import com.bank.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public UserDTO save(@RequestBody User user) throws UsernameAlreadyExistsException {
        return userService.save(user);
    }

    @DeleteMapping("/{username}")
    public UserDTO deleteUser(@PathVariable("username") String username) {
        return userService.deleteUser(username);
    }

    @GetMapping(value = "/login")
    public UserDTO login(@RequestBody UserLoginDTO userLoginDTO) throws InvalidPasswodException, UsernameNotFoundException, UserAlreadyLoggedException {
        return userService.login(userLoginDTO);
    }

    @DeleteMapping(value = "/logout")
    public UserDTO logout(@RequestParam String token) throws SessionNotFoundException {
        return userService.logout(token);
    }

}
