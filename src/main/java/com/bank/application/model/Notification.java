package com.bank.application.model;

import com.bank.application.util.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 58)
    private String details;

    @Column(name = "created_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdTime;

    @Column(name = "sent_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime sentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public Notification(NotificationBuilder notificationBuilder) {
        this.user = notificationBuilder.user;
        this.details = notificationBuilder.details;
        this.createdTime = notificationBuilder.createdTime;
        this.sentTime = notificationBuilder.sentTime;
        this.status = notificationBuilder.status;
    }

    public Notification() {
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    @PrePersist
    void preInsert() {
        if (this.createdTime == null)
            this.createdTime = LocalDateTime.now();
        if (this.sentTime == null)
            this.sentTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static class NotificationBuilder {

        private User user;
        private String details;
        private LocalDateTime createdTime;
        private LocalDateTime sentTime;
        private Status status;

        public NotificationBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public NotificationBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public NotificationBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public NotificationBuilder withSentTime(LocalDateTime sentTime) {
            this.sentTime = sentTime;
            return this;
        }

        public NotificationBuilder withStatus(Status status) {
            this.status = status;
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }
}
