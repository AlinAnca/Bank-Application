package com.bank.application.model.DTO.converter;


import com.bank.application.model.Account;
import com.bank.application.model.DTO.AccountDTO;
import com.bank.application.model.DTO.AccountRequestDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountConverter {

    public List<AccountDTO> convertToListAccountDTO(List<Account> accounts) {
        return accounts.stream()
                .map(account -> convertToAccountDTO(account))
                .collect(Collectors.toList());
    }

    public AccountDTO convertToAccountDTO(Account account) {
        return AccountDTO.builder()
                .withUser(account.getUser())
                .withAccountNumber(account.getAccountNumber())
                .withBalance(account.getBalance())
                .withCurrency(account.getCurrency())
                .withCreatedTime(account.getCreatedTime())
                .build();
    }

    public static AccountRequestDTO convertToAccountRequestDTO(Account account) {
        return AccountRequestDTO.builder()
                .withAccountNumber(account.getAccountNumber())
                .withBalance(account.getBalance())
                .withCurrency(account.getCurrency())
                .build();
    }
}
