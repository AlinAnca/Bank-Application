package com.bank.application.controller;

import com.bank.application.exceptions.*;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.TransferRequestDTO;
import com.bank.application.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer(@RequestParam String token, @Valid @RequestBody TransferRequestDTO transferRequestDTO) throws SessionNotFoundException, UniqueAccountException, UserNotFoundException, InvalidAmountException, InvalidCurrencyException, DuplicateAccountNumberException {
        return new ResponseEntity<>(transferService.saveTransfer(token, transferRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public List<TransferDTO> findAllTransfers(@RequestParam String token) throws SessionNotFoundException, UserNotFoundException {
        return transferService.findAllTransfersByToken(token);
    }
}
