package com.coding.example.bank_account_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class StatementDTO {

    private String firstName;
    private String lastName;
    private Integer accountNumber;
    private Date date;
    private Double balance;
    private List<TransactionDTO> transactions;

    private StatementDTO() {
    }

    private StatementDTO(String firstName, String lastName, Integer accountNumber, Date date, Double balance, List<TransactionDTO> transactions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountNumber = accountNumber;
        this.date = date;
        this.balance = balance;
        this.transactions = transactions;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
    public Date getDate() {
        return date;
    }

    public Double getBalance() {
        return balance;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public static StatementDTOBuilder newBuilder() {
        return new StatementDTOBuilder();
    }

    public static class StatementDTOBuilder {

        private String firstName;
        private String lastName;
        private Integer accountNumber;
        private Date date;
        private Double balance;
        private List<TransactionDTO> transactions;


        public StatementDTOBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StatementDTOBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StatementDTOBuilder setAccountNumber(Integer accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public StatementDTOBuilder setDate(Date date) {
            this.date = date;
            return this;
        }

        public StatementDTOBuilder setBalance(Double balance) {
            this.balance = balance;
            return this;
        }

        public StatementDTOBuilder setTransactions(List<TransactionDTO> transactions) {
            this.transactions = transactions;
            return this;
        }

        public StatementDTO build() {
            return new StatementDTO(firstName, lastName, accountNumber, date, balance, transactions);
        }
    }
}
