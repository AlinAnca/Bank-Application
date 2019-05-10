package com.bank.application.util;

import com.bank.application.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private SessionFactoryUtil(){}

    public static synchronized SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User .class)
                    .addAnnotatedClass(Person .class)
                    .addAnnotatedClass(Notification .class)
                    .addAnnotatedClass(Account .class)
                    .addAnnotatedClass(Transaction .class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}
