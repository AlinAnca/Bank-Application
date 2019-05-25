package com.bank.application.model.DTO.converter;

import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.Transfer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransferConverter {

    public List<TransferDTO> convertToListTransferDTO(List<Transfer> transfers) {
        return transfers.stream()
                .map(transfer -> convertToTransferDTO(transfer))
                .collect(Collectors.toList());
    }

    public TransferDTO convertToTransferDTO(Transfer transfer) {
        return TransferDTO.builder()
                .withAccountNumber(transfer.getAccountNumber())
                .withAmount(transfer.getAmount())
                .withAccountTo(transfer.getAccount().getAccountNumber())
                .withDetails(transfer.getDetails())
                .withType(transfer.getType())
                .withCreatedTime(transfer.getCreatedTime())
                .build();
    }
}
