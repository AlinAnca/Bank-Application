package com.bank.application.repository;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "select COUNT(*) from account where account_type = 'RON'", nativeQuery = true)
    Integer countAccountsByCurrencyRON();

    @Query(value = "select COUNT(*) from account where account_type = 'EUR'", nativeQuery = true)
    Integer countAccountsByCurrencyEUR();

    Account save(Account account);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    List<Account> findAlLAccountsByUser(User user);

    @Query(value = "select * from account a join authentication b ON a.user_id = b.reference where b.token = ?1",
            nativeQuery = true)
    List<Account> findAllAccountsByToken(String token);

    @Modifying
    @Query("update Account a set a.balance = a.balance - ?1, a.updatedTime = current_date where a.accountNumber = ?2")
    Integer updateAccountFromBalance(BigDecimal amount, String accountNumber);

    @Modifying
    @Query("update Account a set a.balance = a.balance + ?1, a.updatedTime = current_timestamp where a.accountNumber = ?2")
    Integer updateAccountToBalance(BigDecimal amount, String accountNumber);
}
