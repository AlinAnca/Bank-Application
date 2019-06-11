package com.bank.application.repository;

import com.bank.application.model.Notification;
import com.bank.application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select * from notification where status = 'NOT_SENT'", nativeQuery = true)
    List<Notification> findNotificationsNotSent();

    Notification findNotificationByUser(User user);

    Notification save(Notification notification);

    @Modifying
    @Query("update Notification set status = 'SENT', sentTime = current_timestamp where id = ?1")
    Integer updateNotificationStatusById(Long id);
}
