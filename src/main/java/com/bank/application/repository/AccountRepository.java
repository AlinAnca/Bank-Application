package com.bank.application.repository;

import com.bank.application.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
    private static final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Person.class)
            .addAnnotatedClass(Notification.class)
            .addAnnotatedClass(Account.class)
            .addAnnotatedClass(Transaction.class)
            .buildSessionFactory();

    private static final String SQL_ACCOUNTS = "select * from account a "
            + "join user b ON a.user_id = b.id "
            + "where a.user_id = :user_id";

    /**
     * Gets data from file and adds it into a collection of accounts.
     * Catches invalid data and sends warnings to the user.
     *
     * @return the collection of available accounts
     */
    public static Optional<List<Account>> getAccounts() {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;

        List<Account> accounts;
        try {
            tx = session.beginTransaction();
            accounts = session.createQuery("FROM com.bank.application.model.Account").list();
            tx.commit();
            if (accounts.size() > 0) {
                return Optional.of(accounts);
            }
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        } finally {
            session.close();
        }
        return Optional.empty();
    }

    public static List<Account> getAccountsFor(User user) {
        Session session = sessionFactory.getCurrentSession();
        org.hibernate.Transaction tx = null;

        List<Account> accounts = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            NativeQuery query = session.createNativeQuery(SQL_ACCOUNTS);
            query.addEntity(Account.class);
            query.setParameter("user_id", user.getId());
            accounts = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        } finally {
            session.close();
        }
        return accounts;
    }

    public static Long addAccount(Account account) {
        Session session = sessionFactory.getCurrentSession();
        org.hibernate.Transaction tx = null;
        Long accountID = null;

        try {
            tx = session.beginTransaction();
            accountID = (Long) session.save(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        } finally {
            session.close();
        }
        return accountID;
    }

    public static void updateAccountBalance(Long accountID, BigDecimal newBalance) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Account account = session.get(Account.class, accountID);
            account.setBalance(newBalance);
            account.setUpdatedTime(LocalDateTime.now());
            session.update(account);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        } finally {
            session.close();
        }
    }
}
