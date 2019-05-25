package com.bank.application.service.impl;

import com.bank.application.exceptions.SessionNotFoundException;
import com.bank.application.model.Account;
import com.bank.application.model.Authentication;
import com.bank.application.model.DTO.TransferDTO;
import com.bank.application.model.DTO.TransferRequestDTO;
import com.bank.application.model.DTO.converter.TransferConverter;
import com.bank.application.model.Transfer;
import com.bank.application.model.User;
import com.bank.application.repository.AccountRepository;
import com.bank.application.repository.AuthenticationRepository;
import com.bank.application.repository.TransferRepository;
import com.bank.application.repository.UserRepository;
import com.bank.application.service.TransferService;
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

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    TransferConverter transferConverter;

    @Override
    public TransferDTO saveTransfer(String token, TransferRequestDTO transferRequestDTO) throws SessionNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            String accountNumberFrom = transferRequestDTO.getAccountNumber();
            Account accountFrom = accountRepository.findAccountByAccountNumber(transferRequestDTO.getAccountNumber()).get();

            String accountNumberTo = transferRequestDTO.getAccount().getAccountNumber();
            Account accountTo = accountRepository.findAccountByAccountNumber(accountNumberTo).get();

            Transfer incomingTransfer = Transfer.builder()
                    .withAccountNumber(accountNumberFrom)
                    .withAmount(transferRequestDTO.getAmount())
                    .withAccount(accountTo)
                    .withDetails(transferRequestDTO.getDetails())
                    .withType(Type.INCOMING).build();

            BigDecimal newBalanceFrom = accountFrom.getBalance().subtract(transferRequestDTO.getAmount());
            accountRepository.updateAccountBalance(newBalanceFrom, accountNumberFrom);

            Transfer outgoingTransfer = Transfer.builder()
                    .withAccountNumber(accountNumberTo)
                    .withAmount(transferRequestDTO.getAmount())
                    .withAccount(accountFrom)
                    .withDetails(transferRequestDTO.getDetails())
                    .withType(Type.OUTGOING).build();

            BigDecimal newBalanceTo = accountTo.getBalance().add(transferRequestDTO.getAmount());
            accountRepository.updateAccountBalance(newBalanceTo, accountNumberTo);

            transferRepository.save(incomingTransfer);
            transferRepository.save(outgoingTransfer);
            return transferConverter.convertToTransferDTO(incomingTransfer);
        } else
            throw new SessionNotFoundException("User not logged. Please login and try again..");
    }

    @Override
    public List<TransferDTO> findAllTransfersByToken(String token) throws SessionNotFoundException {
        Optional<Authentication> authentication = authenticationRepository.findAuthenticationByToken(token);
        if (authentication.isPresent()) {
            User user = userRepository.findUserById(authentication.get().getReference()).get();
            List<Account> accounts = accountRepository.findAlLAccountsByUser(user);
            List<Transfer> transfers = new ArrayList<>();
            for (Account account : accounts) {
                transfers.addAll(transferRepository.findTransfersByAccount(account));
            }
            return transferConverter.convertToListTransferDTO(transfers);
        } else
            throw new SessionNotFoundException("User not logged. Please login and try again..");

    }


}
