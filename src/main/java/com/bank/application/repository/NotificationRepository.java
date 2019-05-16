package com.bank.application.repository;

import com.bank.application.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Notification save(Notification notification);
}
