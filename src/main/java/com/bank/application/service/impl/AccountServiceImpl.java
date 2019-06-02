package com.bank.application.service.impl;

import com.bank.application.exceptions.AccountAlreadyExistsException;
import com.bank.application.exceptions.InvalidCurrencyException;
import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.exceptions.UserNotFoundException;
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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private UserRepository userRepository;
    private AuthenticationRepository authenticationRepository;
    private AccountRepository accountRepository;
    private AccountConverter accountConverter;

    @Autowired
    public AccountServiceImpl(UserRepository userRepository, AuthenticationRepository authenticationRepository, AccountRepository accountRepository, AccountConverter accountConverter) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.accountRepository = accountRepository;
        this.accountConverter = accountConverter;
    }

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> findAllAccountsByToken(String token) {
        return accountConverter.convertToListAccountDTO(accountRepository.findAllAccountsByToken(token));
    }

    @Override
    public AccountDTO saveAccount(String token, AccountRequestDTO accountRequestDTO) throws SessionNotFoundException, UserNotFoundException, AccountAlreadyExistsException, InvalidCurrencyException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            validateAccount(accountRequestDTO);
            Long userId = authentication.get().getReference();
            Optional<User> user = userRepository.findUserById(userId);
            if (user.isPresent()) {
                Account account = Account.builder()
                        .withUser(user.get())
                        .withAccountNumber(accountRequestDTO.getAccountNumber())
                        .withBalance(accountRequestDTO.getBalance())
                        .withCurrency(accountRequestDTO.getCurrency())
                        .build();
                return accountConverter.convertToAccountDTO(accountRepository.save(account));
            } else
                throw new UserNotFoundException("User with id '" + userId + "' not found.");
        } else
            throw new SessionNotFoundException("User not logged. Please login and try again..");
    }

    private void validateAccount(AccountRequestDTO accountRequestDTO) throws AccountAlreadyExistsException, InvalidCurrencyException {
        if (accountRepository.findAccountByAccountNumber(accountRequestDTO.getAccountNumber()).isPresent()) {
            throw new AccountAlreadyExistsException("Account already exists!");
        }
    }
}
