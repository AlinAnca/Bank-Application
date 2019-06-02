package com.bank.application.controller;

import com.bank.application.exceptions.*;
import com.bank.application.model.DTO.TransferRequestDTO;
import com.bank.application.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    TransferService transferService;

    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestParam String token, @Valid @RequestBody TransferRequestDTO transferRequestDTO) throws SessionNotFoundException, UniqueAccountException, UserNotFoundException, InvalidAmountException, InvalidCurrencyException, DuplicateAccountNumberException {
        return new ResponseEntity<>(transferService.saveTransfer(token, transferRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAllTransfers(@RequestParam String token) throws SessionNotFoundException, UserNotFoundException {
        return new ResponseEntity<>(transferService.findAllTransfersByToken(token), HttpStatus.OK);
    }
}
