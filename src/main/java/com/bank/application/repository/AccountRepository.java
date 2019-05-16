package com.bank.application.repository;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    List<Account> findAll();

    Account save(Account account);

    @Query(value = "select * from account a join user b ON a.user_id = b.id where a.user_id = ?1",
            nativeQuery = true)
    List<Account> findAccountsBy(User user);

    @Modifying
    @Query("update Account a set a.balance = ?1, a.updatedTime = current_date where a.id = ?2")
    Account updateAccountBalance(BigDecimal newBalance, Long accountID);
}
