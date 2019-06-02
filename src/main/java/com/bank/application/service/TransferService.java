package com.bank.application.service;

import com.bank.application.exceptions.*;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.TransferRequestDTO;

import java.util.List;

public interface TransferService {

    TransferDTO saveTransfer(String token, TransferRequestDTO transferRequestDTO) throws SessionNotFoundException, UserNotFoundException, UniqueAccountException, DuplicateAccountNumberException, InvalidCurrencyException, InvalidAmountException;

    List<TransferDTO> findAllTransfersByToken(String token) throws SessionNotFoundException, UserNotFoundException;
}
