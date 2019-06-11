package com.bank.application.model;

import com.bank.application.util.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "account", length = 24)
    private String accountNumber;

    @Positive
    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "details", length = 100, columnDefinition = "varchar(100) default '-'")
    private String details;

    @Column(name = "created_time", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdTime;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 10)
    private Type type;

    public Transfer(TransferBuilder transactionBuilder) {
        this.accountNumber = transactionBuilder.accountNumber;
        this.amount = transactionBuilder.amount;
        this.details = transactionBuilder.details;
        this.createdTime = transactionBuilder.createdTime;
        this.account = transactionBuilder.account;
        this.type = transactionBuilder.type;
    }

    public Transfer() {
    }

    public static TransferBuilder builder() {
        return new TransferBuilder();
    }

    @PrePersist
    void preInsert() {
        if (this.createdTime == null)
            this.createdTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static class TransferBuilder {

        private String accountNumber;
        private BigDecimal amount;
        private String details;
        private LocalDateTime createdTime;
        private Account account;
        private Type type;

        public TransferBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public TransferBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransferBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public TransferBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public TransferBuilder withAccount(Account account) {
            this.account = account;
            return this;
        }

        public TransferBuilder withType(Type type) {
            this.type = type;
            return this;
        }

        public Transfer build() {
            return new Transfer(this);
        }
    }
}
