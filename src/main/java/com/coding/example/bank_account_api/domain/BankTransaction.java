package com.coding.example.bank_account_api.domain;

import com.coding.example.bank_account_api.domainvalue.TransactionType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bank_transaction")
public class BankTransaction {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;
    @Column(nullable = false, scale = 2)
    private Double amount;
    @Column(nullable = false, scale = 2)
    private Double updatedBalance;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount bankAccount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;


    public BankTransaction() {
    }

    public BankTransaction(TransactionType transactionType, Double amount, Double updatedBalance) {
        this(transactionType, amount, updatedBalance, null);
    }

    public BankTransaction(TransactionType transactionType, Double amount, Double updatedBalance, BankAccount bankAccount) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.updatedBalance = updatedBalance;
        this.bankAccount = bankAccount;
        this.transactionDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Double getAmount() {
        return amount;
    }


    public Double getUpdatedBalance() {
        return updatedBalance;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }
}
