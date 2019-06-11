package com.bank.application.service;

import com.bank.application.exceptions.AccountAlreadyExistsException;
import com.bank.application.exceptions.InvalidCurrencyException;
import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.exceptions.UserNotFoundException;
import com.bank.application.model.Account;
import com.bank.application.model.DTO.AccountDTO;
import com.bank.application.model.DTO.AccountRequestDTO;

import java.util.List;

public interface AccountService {

    Account save(Account account);

    List<AccountDTO> findAllAccountsByToken(String token) throws SessionNotFoundException;

    AccountDTO saveAccount(String token, AccountRequestDTO accountRequestDTO) throws SessionNotFoundException, UserNotFoundException, AccountAlreadyExistsException, InvalidCurrencyException;
}
