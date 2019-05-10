package com.bank.application.repository;

import com.bank.application.model.Notification;
import com.bank.application.util.SessionFactoryUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.logging.Logger;

public class NotificationRepository {
    private static final Logger LOGGER = Logger.getLogger(NotificationRepository.class.getName());

    public static Long addNotification(Notification notification) {
        Session session = SessionFactoryUtil.getSessionFactory().openSession();
        org.hibernate.Transaction tx = null;
        Long transactionID = null;

        try {
            tx = session.beginTransaction();
            transactionID = (Long) session.save(notification);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            LOGGER.finest(e.getLocalizedMessage());
        } finally {
            session.close();
        }
        return transactionID;
    }
}
