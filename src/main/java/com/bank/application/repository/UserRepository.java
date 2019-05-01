package com.bank.application.repository;

import com.bank.application.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * UserCollection is the class which converts the data
 * from Database into a collection of users.
 */
public class UserRepository {
    private static final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Person.class)
            .addAnnotatedClass(Notification.class)
            .addAnnotatedClass(Account.class)
            .addAnnotatedClass(Transaction.class)
            .buildSessionFactory();

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

    /**
     * Gets data from file and adds it into a collection of users.
     * Catches invalid data and sends warnings.
     *
     * @return the collection of available users
     */
    public static List<User> getUsers() {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction tx = null;

        List<User> users = new ArrayList<>();
        try {
            tx = session.beginTransaction();
            users = session.createQuery("FROM com.bank.application.model.User").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        } finally {
            session.close();
        }
        return users;
    }
}