package com.bank.application.controller;

import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.model.DTO.TransferRequestDTO;
import com.bank.application.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    @Autowired
    TransferService transferService;

    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestParam String token, @RequestBody TransferRequestDTO transferRequestDTO) throws SessionNotFoundException {
        return new ResponseEntity<>(transferService.saveTransfer(token, transferRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> findAllTransfers(@RequestParam String token) throws SessionNotFoundException {
        return new ResponseEntity<>(transferService.findAllTransfersByToken(token), HttpStatus.OK);
    }
}
