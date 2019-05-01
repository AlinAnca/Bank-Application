package com.bank.application.repository;

import com.bank.application.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Logger;

public class TransactionRepository {
    private static final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Person.class)
            .addAnnotatedClass(Notification.class)
            .addAnnotatedClass(Account.class)
            .addAnnotatedClass(Transaction.class)
            .buildSessionFactory();

    private static final Logger LOGGER = Logger.getLogger(TransactionRepository.class.getName());

    public static Long addTransaction(Transaction transaction) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;
        Long transactionID = null;

        try {
            tx = session.beginTransaction();
            transactionID = (Long) session.save(transaction);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getLocalizedMessage());
        } finally {
            session.close();
        }
        return transactionID;
    }
}
