package com.bank.application.repository;

import com.bank.application.model.Account;
import com.bank.application.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findTransfersByAccount(Account account);

    Transfer save(Transfer transfer);
}
