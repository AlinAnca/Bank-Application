package com.bank.application.model.DTO;

import com.bank.application.model.Account;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferRequestDTO {
    @NotNull
    @Length(min = 24, max = 24)
    private String accountNumber;

    @NotNull
    private BigDecimal amount;

    private String details;

    @NotNull
    private Account account;

    public TransferRequestDTO(TransferRequestDTOBuilder builder) {
        this.accountNumber = builder.accountNumber;
        this.amount = builder.amount;
        this.details = builder.details;
        this.account = builder.account;
    }

    public TransferRequestDTO() {
    }

    public static TransferRequestDTOBuilder builder() {
        return new TransferRequestDTOBuilder();
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static class TransferRequestDTOBuilder {

        private String accountNumber;
        private BigDecimal amount;
        private String details;
        private Account account;

        public TransferRequestDTOBuilder withAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public TransferRequestDTOBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransferRequestDTOBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public TransferRequestDTOBuilder withAccount(Account account) {
            this.account = account;
            return this;
        }

        public TransferRequestDTO build() {
            return new TransferRequestDTO(this);
        }
    }
}
