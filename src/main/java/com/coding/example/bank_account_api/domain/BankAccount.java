package com.coding.example.bank_account_api.domain;

import com.coding.example.bank_account_api.domainvalue.AccountType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bank_account",
        uniqueConstraints = @UniqueConstraint(name = "uc_accountNumber", columnNames = {"accountNumber"}))
public class BankAccount {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Integer accountNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;
    @Column(nullable = false, scale = 2)
    private Double balance;
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BankTransaction> transactions;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    public BankAccount() {
    }

    public BankAccount(Integer accountNumber, AccountType accountType, Customer customer) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.customer = customer;
        this.dateCreated = new Date();
        this.balance = new Double(0.00d);
        this.transactions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void add(Double amount) {
        BigDecimal bigBalance = new BigDecimal(this.balance).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal bigAmont = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        bigBalance = bigBalance.add(bigAmont);
        this.balance = Double.valueOf(bigBalance.doubleValue());
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<BankTransaction> getTransactions() {
        return transactions;
    }

    public void addBankTransaction(BankTransaction bankTransaction) {
        bankTransaction.setBankAccount(this);
        transactions.add(bankTransaction);
    }
}
