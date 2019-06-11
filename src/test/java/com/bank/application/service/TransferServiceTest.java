package com.bank.application.service;

import com.bank.application.exceptions.*;
import com.bank.application.model.*;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.converter.TransferConverter;
import com.bank.application.repository.*;
import com.bank.application.service.impl.TransferServiceImpl;
import com.bank.application.util.Currency;
import com.bank.application.util.Status;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private Account accountTO;
    private Transfer transfer;
    private Transfer transferIN;
    private Notification notification;
    private Account accountWithInvalidAmount;
    private Account accountWithInvalidType;

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

        accountTO = Account.builder()
                .withUser(user)
                .withAccountNumber("RO28BRDE450SV21000004222")
                .withBalance(BigDecimal.valueOf(100))
                .withCurrency(Currency.EUR)
                .build();

        accountWithInvalidAmount = Account.builder()
                .withUser(user)
                .withAccountNumber("RO28BRDE450SV21000004555")
                .withBalance(BigDecimal.valueOf(2))
                .withCurrency(Currency.EUR)
                .build();

        accountWithInvalidType = Account.builder()
                .withUser(user)
                .withAccountNumber("RO28BRDE450SV2100000TEST")
                .withBalance(BigDecimal.valueOf(200))
                .withCurrency(Currency.RON)
                .build();

        authentication = Authentication.builder()
                .withReference(user.getId())
                .withToken("testToken")
                .build();

        transfer = Transfer.builder()
                .withAccountNumber("RO28BRDE450SV21000004555")
                .withAmount(BigDecimal.valueOf(100))
                .withAccount(accountTO)
                .withDetails("Test for transfers")
                .withType(Type.OUTGOING).build();

        transferIN = Transfer.builder()
                .withAccountNumber("RO28BRDE450SV2100000222")
                .withAmount(BigDecimal.valueOf(100))
                .withAccount(account)
                .withDetails("Test for transfers")
                .withType(Type.INCOMING).build();


        notification = Notification.builder()
                .withUser(user)
                .withDetails("Transfer money")
                .withStatus(Status.SENT)
                .build();

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
    public void saveTransfer_Success() throws DuplicateAccountNumberException, UniqueAccountException, UserNotFoundException, InvalidAmountException, SessionNotFoundException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));

        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account), Optional.of(accountTO));
        when(accountRepository.countAccountsByCurrencyEUR()).thenReturn(2);

        when(userRepository.findUserById(any())).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        when(transferRepository.save(any(Transfer.class))).thenReturn(transfer).thenReturn(transferIN);

        TransferDTO transferDTO = transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        assertNotNull(transferDTO);
    }

    @Test
    public void saveTransfer_EmptyToken_Throws_SessionNotFoundException() {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> {
            transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        });
    }

    @Test
    public void saveTransfer_InvalidAmount_Throws_InvalidAmountException() {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(accountWithInvalidAmount), Optional.of(accountTO));
        assertThrows(InvalidAmountException.class, () -> {
            transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        });
    }

    @Test
    public void saveTransfer_DifferentAccountTypes_Throws_InvalidCurrencyException() {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account), Optional.of(accountWithInvalidType));
        assertThrows(InvalidCurrencyException.class, () -> {
            transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        });
    }

    @Test
    public void saveTransfer_ToSameAccount_Throws_DuplicateAccountNumberException() throws DuplicateAccountNumberException, UniqueAccountException, UserNotFoundException, InvalidAmountException, SessionNotFoundException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account));
        assertThrows(DuplicateAccountNumberException.class, () -> {
            transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        });
    }

    @Test
    public void saveTransfer_UniqueAccountEUR_Throws_UniqueAccountException() throws DuplicateAccountNumberException, UniqueAccountException, UserNotFoundException, InvalidAmountException, SessionNotFoundException, InvalidCurrencyException {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account), Optional.of(accountTO));
        when(accountRepository.countAccountsByCurrencyEUR()).thenReturn(1);
        assertThrows(UniqueAccountException.class, () -> {
            transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        });
    }

    @Test
    public void saveTransfer_NoUserFound_Throws_UserNotFoundException() {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(accountRepository.findAccountByAccountNumber(anyString())).thenReturn(Optional.of(account), Optional.of(accountTO));
        when(accountRepository.countAccountsByCurrencyEUR()).thenReturn(2);
        when(userRepository.findUserById(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            transferService.saveTransfer("testToken", transferConverter.convertToTransferRequestDTO(transfer));
        });
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

    @Test
    public void findAllTransfersBy_EmptyToken_Throws_SessionNotFoundException() {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.empty());
        assertThrows(SessionNotFoundException.class, () -> {
            transferService.findAllTransfersByToken("testToken");
        });
    }

    @Test
    public void findAllTransfersBy_NoUser_Throws_UserNotFoundException() {
        when(authenticationRepository.findAuthenticationByToken(anyString())).thenReturn(Optional.of(authentication));
        when(userRepository.findUserById(any())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            transferService.findAllTransfersByToken("testToken");
        });
    }
}
