package com.bank.application.service.impl;

import com.bank.application.exceptions.*;
import com.bank.application.model.*;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.TransferRequestDTO;
import com.bank.application.model.DTO.converter.TransferConverter;
import com.bank.application.repository.*;
import com.bank.application.service.TransferService;
import com.bank.application.util.Currency;
import com.bank.application.util.Status;
import com.bank.application.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransferServiceImpl implements TransferService {

    private UserRepository userRepository;
    private AuthenticationRepository authenticationRepository;
    private AccountRepository accountRepository;
    private TransferRepository transferRepository;
    private TransferConverter transferConverter;
    private NotificationRepository notificationRepository;

    @Autowired
    public TransferServiceImpl(UserRepository userRepository, AuthenticationRepository authenticationRepository,
                               AccountRepository accountRepository, TransferRepository transferRepository,
                               TransferConverter transferConverter, NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.authenticationRepository = authenticationRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.transferConverter = transferConverter;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public TransferDTO saveTransfer(String token, TransferRequestDTO transferRequestDTO) throws SessionNotFoundException, UserNotFoundException, UniqueAccountException, DuplicateAccountNumberException, InvalidCurrencyException, InvalidAmountException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            String accountNumberFrom = transferRequestDTO.getAccountNumber();
            Account accountFrom = accountRepository.findAccountByAccountNumber(transferRequestDTO.getAccountNumber()).get();
            String accountNumberTo = transferRequestDTO.getAccount().getAccountNumber();
            Account accountTo = accountRepository.findAccountByAccountNumber(accountNumberTo).get();

            BigDecimal amount = transferRequestDTO.getAmount();
            String details = transferRequestDTO.getDetails();

            validateTransfer(accountFrom, accountTo, amount);

            Transfer incomingTransfer = Transfer.builder()
                    .withAccountNumber(accountNumberFrom)
                    .withAmount(amount)
                    .withAccount(accountTo)
                    .withDetails(details)
                    .withType(Type.INCOMING).build();

            Transfer outgoingTransfer = Transfer.builder()
                    .withAccountNumber(accountNumberTo)
                    .withAmount(amount)
                    .withAccount(accountFrom)
                    .withDetails(details)
                    .withType(Type.OUTGOING).build();

            accountRepository.updateAccountFromBalance(amount, accountNumberFrom);
            accountRepository.updateAccountToBalance(amount, accountNumberTo);

            transferRepository.save(incomingTransfer);
            transferRepository.save(outgoingTransfer);

            createNotification(authentication.get(), details);

            return transferConverter.convertToTransferDTO(incomingTransfer);

        } else
            throw new SessionNotFoundException("User not logged. Please login and try again..");
    }

    private Notification createNotification(Authentication authentication, String details) throws UserNotFoundException {
        Optional<User> user = userRepository.findUserById(authentication.getReference());
        if (user.isPresent()) {
            return notificationRepository.save(Notification.builder()
                    .withUser(user.get())
                    .withDetails(details)
                    .withStatus(Status.NOT_SENT)
                    .build());
        } else
            throw new UserNotFoundException("User could not be notify!");
    }

    private void validateTransfer(Account accountFrom, Account accountTo, BigDecimal amount) throws DuplicateAccountNumberException, InvalidCurrencyException, UniqueAccountException, InvalidAmountException {
        checkAccountUniqueness(accountFrom);
        if (accountFrom.getBalance().subtract(amount).doubleValue() < 0) {
            throw new InvalidAmountException();
        }
        if (!accountFrom.getCurrency().equals(accountTo.getCurrency())) {
            throw new InvalidCurrencyException("Different account types!");
        }
        if (accountFrom.getAccountNumber().equals(accountTo.getAccountNumber())) {
            throw new DuplicateAccountNumberException("Duplicate accounts found!");
        }
    }

    private void checkAccountUniqueness(Account account) throws UniqueAccountException {
        if (Currency.EUR.equals(account.getCurrency()) && accountRepository.countAccountsByCurrencyEUR() == 1
                || Currency.RON.equals(account.getCurrency()) && accountRepository.countAccountsByCurrencyRON() == 1) {
            throw new UniqueAccountException("Only one account found of type " + account.getCurrency());
        }
    }

    @Override
    public List<TransferDTO> findAllTransfersByToken(String token) throws SessionNotFoundException, UserNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            Long userId = authentication.get().getReference();
            Optional<User> user = userRepository.findUserById(userId);
            if (user.isPresent()) {
                List<Account> accounts = accountRepository.findAlLAccountsByUser(user.get());
                List<Transfer> transfers = new ArrayList<>();
                for (Account account : accounts) {
                    transfers.addAll(transferRepository.findTransfersByAccount(account));
                }
                return transferConverter.convertToListTransferDTO(transfers);
            } else
                throw new UserNotFoundException("User with id '" + userId + "' not found.");

        } else
            throw new SessionNotFoundException("User not logged. Please login and try again..");

    }
}
