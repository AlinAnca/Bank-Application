package com.bank.application.controller;

import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.model.Account;
import com.bank.application.model.DTO.AccountDTO;
import com.bank.application.model.DTO.AccountRequestDTO;
import com.bank.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping
    public List<AccountDTO> findAllAccountsForUserLogged(@RequestParam String token) {
        return accountService.findAllAccountsByToken(token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccountForUserLogged(@RequestParam String token, @RequestBody AccountRequestDTO accountRequestDTO) throws SessionNotFoundException {
        return new ResponseEntity<>(accountService.saveAccount(token, accountRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/")
    public ResponseEntity<?> saveAccount(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.save(account), HttpStatus.CREATED);
    }
}
