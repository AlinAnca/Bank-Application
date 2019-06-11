package com.bank.application.model.DTO;

import com.bank.application.util.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransferDTO {
    private String accountNumber;
    private BigDecimal amount;
    private String theOtherAccountNumber;
    private String details;
    private LocalDateTime createdTime;
    private Type type;

    public TransferDTO(TransferDTOBuilder builder) {
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.theOtherAccountNumber = builder.theOtherAccountNumber;
        this.details = builder.details;
        this.createdTime = builder.createdTime;
        this.type = builder.type;
    }

    public TransferDTO() {
    }

    public static TransferDTOBuilder builder() {
        return new TransferDTOBuilder();
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

    public String getTheOtherAccountNumber() {
        return theOtherAccountNumber;
    }

    public void setTheOtherAccountNumber(String theOtherAccountNumber) {
        this.theOtherAccountNumber = theOtherAccountNumber;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static class TransferDTOBuilder {

        private String accountNumber;
        private BigDecimal amount;
        private String theOtherAccountNumber;
        private String details;
        private LocalDateTime createdTime;
        private Type type;

        public TransferDTOBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public TransferDTOBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransferDTOBuilder withAccountTo(String theOtherAccountNumber) {
            this.theOtherAccountNumber = theOtherAccountNumber;
            return this;
        }

        public TransferDTOBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public TransferDTOBuilder withCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public TransferDTOBuilder withType(Type type) {
            this.type = type;
            return this;
        }

        public TransferDTO build() {
            return new TransferDTO(this);
        }
    }
}
