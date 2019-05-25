package com.bank.application.service.impl;

import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.model.Account;
import com.bank.application.model.Authentication;
import com.bank.application.model.DTO.AccountDTO;
import com.bank.application.model.DTO.AccountRequestDTO;
import com.bank.application.model.DTO.converter.AccountConverter;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.AuthenticationRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountConverter accountConverter;

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> findAllAccountsByToken(String token) {
        return accountConverter.convertToListAccountDTO(accountRepository.findAllAccountsByToken(token));
    }

    @Override
    public AccountDTO saveAccount(String token, AccountRequestDTO accountRequestDTO) throws SessionNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            User user = userRepository.findUserById(authentication.get().getReference()).get();
            System.out.println(accountRequestDTO.getCurrency());
            Account account = Account.builder()
                    .withUser(user)
                    .withAccountNumber(accountRequestDTO.getAccountNumber())
                    .withBalance(accountRequestDTO.getBalance())
                    .withCurrency(accountRequestDTO.getCurrency())
                    .build();
            return accountConverter.convertToAccountDTO(accountRepository.save(account));
        } else
            throw new SessionNotFoundException("User not logged. Please login and try again..");
    }
}
