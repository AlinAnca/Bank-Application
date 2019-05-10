package com.bank.application.model;

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

    @Transient
    @Column(name = "created_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdTime;

    @Transient
    @Column(name = "sent_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime sentTime;

    public Notification(NotificationBuilder notificationBuilder) {
        this.user = notificationBuilder.user;
        this.details = notificationBuilder.details;
        this.createdTime = notificationBuilder.createdTime;
        this.sentTime = notificationBuilder.sentTime;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
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

    public static class NotificationBuilder {

        private User user;
        private String details;
        private LocalDateTime createdTime;
        private LocalDateTime sentTime;

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

        public Notification build() {
            return new Notification(this);
        }
    }
}
