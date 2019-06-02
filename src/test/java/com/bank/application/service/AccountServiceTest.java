package com.bank.application.service;

import com.bank.application.exceptions.AccountAlreadyExistsException;
import com.bank.application.exceptions.InvalidCurrencyException;
import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.exceptions.UserNotFoundException;
import com.bank.application.model.Account;
import com.bank.application.model.Authentication;
import com.bank.application.model.DTO.AccountDTO;
import com.bank.application.model.DTO.converter.AccountConverter;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.AuthenticationRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.service.impl.AccountServiceImpl;
import com.bank.application.util.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountService.class)
public class AccountServiceTest {

    private User user;
    private Authentication authentication;
    private Account account;
    private AccountRepository accountRepository;
    private AccountConverter accountConverter;
    private AuthenticationRepository authenticationRepository;
    private UserRepository userRepository;
    private AccountService accountService;

    @Before
    public void setUp() {
        user = User.builder()
                .withId(1L)
                .withUsername("test")
                .withPassword("pass")
                .build();
        account = Account.builder()
                .withUser(user)
                .withAccountNumber("RO28BRDE450SV21000004555")
                .withBalance(BigDecimal.valueOf(200))
                .withCurrency(Currency.EUR)
                .build();
        authentication = Authentication.builder()
                .withReference(user.getId())
                .withToken("testToken")
                .build();
        userRepository = mock(UserRepository.class);
        authenticationRepository = mock(AuthenticationRepository.class);
        accountRepository = mock(AccountRepository.class);
        accountConverter = new AccountConverter();

        accountService = new AccountServiceImpl(userRepository, authenticationRepository, accountRepository, accountConverter);
    }

    @Test
    public void findAllAccountsByToken_Success() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);

        when(accountRepository.findAllAccountsByToken(anyString())).thenReturn(accounts);
        List<AccountDTO> accountsDTO = accountService.findAllAccountsByToken("testToken");
        assertTrue(accountsDTO.size() == 1);
    }

    @Test
    public void findAllAccountsByToken_NoAccounts_ReturnEmptyResult() {
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAllAccountsByToken(anyString())).thenReturn(accounts);
        List<AccountDTO> accountsDTO = accountService.findAllAccountsByToken("testToken");
        assertTrue(accountsDTO.size() == 0);
    }

    @Test
    public void saveAccount_AuthenticatedUser_Success() throws UserNotFoundException, SessionNotFoundException, AccountAlreadyExistsException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(userRepository.findUserById(any())).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO accountDTO = accountService.saveAccount("testToken", AccountConverter.convertToAccountRequestDTO(account));
        assertNotNull(accountDTO);
    }

    @Test(expected = SessionNotFoundException.class)
    public void saveAccount_UserNotAuthenticated_throws_SessionNotFoundException() throws UserNotFoundException, SessionNotFoundException, AccountAlreadyExistsException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.empty());
        accountService.saveAccount("testToken", AccountConverter.convertToAccountRequestDTO(account));
    }

    @Test(expected = UserNotFoundException.class)
    public void saveAccount_EmptyUser_throws_SessionNotFoundException() throws UserNotFoundException, SessionNotFoundException, AccountAlreadyExistsException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(userRepository.findUserById(any())).thenReturn(Optional.empty());
        accountService.saveAccount("testToken", AccountConverter.convertToAccountRequestDTO(account));
    }

    @Test(expected = AccountAlreadyExistsException.class)
    public void saveExistentAccount_throws_AccountAlreadyExistsException() throws UserNotFoundException, SessionNotFoundException, AccountAlreadyExistsException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account));
        accountService.saveAccount("testToken", AccountConverter.convertToAccountRequestDTO(account));
    }
}
