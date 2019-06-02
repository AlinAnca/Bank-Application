package com.bank.application.scheduler;

import com.bank.application.exceptions.UserNotFoundException;
import com.bank.application.model.Notification;
import com.bank.application.model.User;
import com.bank.application.repository.NotificationRepository;
import com.bank.application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Properties;

@Configuration
@Component
@Transactional
@EnableScheduling
public class SendEmailScheduler {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private Logger LOGGER = LoggerFactory.getLogger(SendEmailScheduler.class);

    @Value("${jobs.cronSchedule.expire.time.in_minutes:30}")
    private long value;

    @Value("${jobs.emailSchedule.email:}")
    private String emailFrom;

    @Value("${jobs.emailSchedule.password:}")
    private String password;

    @Autowired
    public SendEmailScheduler(final UserRepository userRepository, final NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "${jobs.emailSchedule:}")
    public void notifyUsers() throws MessagingException, UserNotFoundException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.mail.yahoo.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        // props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        List<Notification> notifications = notificationRepository.findNotificationsNotSent();
        for (Notification notification : notifications) {
            User user = notification.getUser();
            sendEmail(props, user, notification.getDetails());
            notificationRepository.updateNotificationStatusById(notification.getId());
        }
    }

    private void sendEmail(Properties props, User user, String details) throws MessagingException {
        String emailTo = userRepository.findUserEmailById(user.getId());
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailFrom, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailFrom));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(emailTo));
        message.setSubject("Transaction details");

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(details, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
}
