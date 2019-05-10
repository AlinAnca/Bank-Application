package com.bank.application.repository;

import com.bank.application.model.User;
import com.bank.application.util.SessionFactoryUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * UserCollection is the class which converts the data
 * from Database into a collection of users.
 */
public class UserRepository {
    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());
    private static final String SQL_GET_USER_BY_USERNAME = "SELECT * FROM user WHERE username =:usernameParam";

    public static User getUserByUsername(String username) {
        org.hibernate.Transaction tx = null;
        User user = null;
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        try
        {
            tx = session.beginTransaction();
            NativeQuery query = session.createNativeQuery(SQL_GET_USER_BY_USERNAME);
            query.addEntity(User.class);
            query.setParameter("usernameParam", username);
            user = (User) query.uniqueResult();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.fine(e.getMessage());
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * Gets data from file and adds it into a collection of users.
     * Catches invalid data and sends warnings.
     *
     * @return the collection of available users
     */
    public static List<User> getAllUsers() {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
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