package com.coding.example.bank_account_api.dto;

import com.coding.example.bank_account_api.domainvalue.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    private Integer accountNumber;
    private TransactionType transactionType;
    private Double amount;
    private Date transactionDate;
    private Double balance;

    private TransactionDTO() {
    }

    private TransactionDTO(Integer accountNumber, TransactionType transactionType, Double amount, Date transactionDate, Double balance) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.balance = balance;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
    public Date getTransactionDate() {
        return transactionDate;
    }

    public Double getBalance() {
        return balance;
    }

    public static TransactionDTOBuilder newBuilder() {
        return new TransactionDTOBuilder();
    }

    public static class TransactionDTOBuilder {

        private Integer accountNumber;
        private TransactionType transactionType;
        private Double amount;
        private Date transactionDate;
        private Double balance;

        public TransactionDTOBuilder setAccountNumber(Integer accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public TransactionDTOBuilder setTransactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public TransactionDTOBuilder setAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionDTOBuilder setTransactionDate(Date transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionDTOBuilder setBalance(Double balance) {
            this.balance = balance;
            return this;
        }

        public TransactionDTO build() {
            return new TransactionDTO(accountNumber, transactionType, amount, transactionDate, balance);
        }
    }
}
