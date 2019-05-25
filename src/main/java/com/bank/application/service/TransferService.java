package com.bank.application.service;

import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.TransferRequestDTO;

import java.util.List;

public interface TransferService {

    TransferDTO saveTransfer(String token, TransferRequestDTO transferRequestDTO) throws SessionNotFoundException;

    List<TransferDTO> findAllTransfersByToken(String token) throws SessionNotFoundException;
}
