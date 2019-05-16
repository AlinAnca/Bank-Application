package com.bank.application.repository;

import com.bank.application.model.Account;
import com.bank.application.model.User;
import com.bank.application.util.SessionFactoryUtil;
import com.bank.application.view.UserView;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * AccountCollection is the class which converts the data
 * from Database into a collection of accounts.
 */
public class AccountRepository {
    private static final Logger LOGGER = Logger.getLogger(AccountRepository.class.getName());

    private static final String SQL_ACCOUNTS = "select * from account a "
            + "join user b ON a.user_id = b.id "
            + "where a.user_id = :user_id";
    private static final String SQL_GET_ACCOUNT = "SELECT * FROM account " +
            "WHERE account_number =:accountNumberParam";

    public static List<Account> getAccountsFor(UserView userView) {
        org.hibernate.Transaction tx = null;

        List<Account> accounts = new ArrayList<>();
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession())
        {
            tx = session.beginTransaction();
            NativeQuery query = session.createNativeQuery(SQL_ACCOUNTS);
            query.addEntity(Account.class);
            query.setParameter("user_id", userView.getId());
            accounts = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        }
        return accounts;
    }

    public static Optional<Account> getAccountByAccountNumber(String accountNumber) {
        org.hibernate.Transaction tx = null;
        Account account = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            NativeQuery query = session.createNativeQuery(SQL_GET_ACCOUNT);
            query.addEntity(Account.class);
            query.setParameter("accountNumberParam", accountNumber);
            account = (Account) query.uniqueResult();
            tx.commit();
            if (account != null) {
                return Optional.of(account);
            }
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Gets data from file and adds it into a collection of accounts.
     * Catches invalid data and sends warnings to the user.
     *
     * @return the collection of available accounts
     */
    public static Optional<List<Account>> getAccounts() {
        org.hibernate.Transaction tx = null;
        List<Account> accounts;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            accounts = session.createQuery("FROM com.bank.application.model.Account").list();
            tx.commit();
            if (accounts.size() > 0) {
                return Optional.of(accounts);
            }
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        }
        return Optional.empty();
    }

    public static Long addAccount(Account account) {
        org.hibernate.Transaction tx = null;
        Long accountID = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            accountID = (Long) session.save(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        }
        return accountID;
    }

    public static void updateAccountBalance(Long accountID, BigDecimal newBalance) {
        org.hibernate.Transaction tx = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Account account = session.get(Account.class, accountID);
            account.setBalance(newBalance);
            account.setUpdatedTime(LocalDateTime.now());
            session.update(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        }
    }
}




