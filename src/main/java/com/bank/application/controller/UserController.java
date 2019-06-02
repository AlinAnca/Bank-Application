package com.bank.application.controller;

import com.bank.application.exceptions.*;
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
    public ResponseEntity<?> findUserByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@Valid @RequestBody User user) throws UsernameAlreadyExistsException {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.OK);
    }

    @GetMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) throws InvalidPasswodException, UserNotFoundException, UserAlreadyLoggedException {
        return new ResponseEntity<>(userService.login(userLoginDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestParam String token) throws SessionNotFoundException {
        return new ResponseEntity<>(userService.logout(token), HttpStatus.OK);
    }

}
