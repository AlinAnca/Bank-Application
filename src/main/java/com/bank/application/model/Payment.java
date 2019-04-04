package com.bank.application.model;

import com.bank.application.util.Currency;

import java.math.BigDecimal;
import java.util.Objects;

public class Payment {
    private BigDecimal amount;
    private Currency paymentType;

    public Payment(BigDecimal amount, Currency paymentType) {
        this.amount = amount;
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Currency paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(amount, payment.amount) &&
                paymentType == payment.paymentType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, paymentType);
    }
}
