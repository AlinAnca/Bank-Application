package com.bank.application.service;

import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.exceptions.UserNotFoundException;
import com.bank.application.model.*;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.converter.TransferConverter;
import com.bank.application.repository.*;
import com.bank.application.service.impl.TransferServiceImpl;
import com.bank.application.util.Currency;
import com.bank.application.util.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferService.class)
public class TransferServiceTest {

    private User user;
    private Authentication authentication;
    private Account account;
    private Transfer transfer;
    private Notification notification;

    private AccountRepository accountRepository;
    private AuthenticationRepository authenticationRepository;
    private UserRepository userRepository;
    private TransferRepository transferRepository;
    private TransferConverter transferConverter;
    private TransferService transferService;
    private NotificationRepository notificationRepository;

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

        transfer = Transfer.builder()
                .withAccountNumber("RO28BRDE450SV21000004222")
                .withAmount(BigDecimal.valueOf(10))
                .withAccount(account)
                .withDetails("Test for transfers")
                .withType(Type.OUTGOING).build();

        userRepository = mock(UserRepository.class);
        authenticationRepository = mock(AuthenticationRepository.class);
        accountRepository = mock(AccountRepository.class);
        transferRepository = mock(TransferRepository.class);
        transferConverter = new TransferConverter();
        notificationRepository = mock(NotificationRepository.class);

        transferService = new TransferServiceImpl(userRepository, authenticationRepository, accountRepository,
                transferRepository, transferConverter, notificationRepository);
    }

    @Test
    public void findAllTransfersByToken_Success() throws UserNotFoundException, SessionNotFoundException {
        List<Account> accounts = Arrays.asList(account);
        List<Transfer> transfers = Arrays.asList(transfer);

        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(userRepository.findUserById(any())).thenReturn(Optional.of(user));
        when(accountRepository.findAlLAccountsByUser(any(User.class))).thenReturn(accounts);
        when(transferRepository.findTransfersByAccount(any(Account.class))).thenReturn(transfers);

        List<TransferDTO> transferDTOs = transferService.findAllTransfersByToken("testToken");
        assertTrue(transferDTOs.size() == 1);
    }

    @Test(expected = SessionNotFoundException.class)
    public void findAllTransfersBy_EmptyToken_Throws_SessionNotFoundException() throws UserNotFoundException, SessionNotFoundException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.empty());
        transferService.findAllTransfersByToken("testToken");
    }

    @Test(expected = UserNotFoundException.class)
    public void findAllTransfersBy_EmptyToken_Throws_UserNotFoundException() throws UserNotFoundException, SessionNotFoundException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(userRepository.findUserById(any())).thenReturn(Optional.empty());
        transferService.findAllTransfersByToken("testToken");
    }

}
