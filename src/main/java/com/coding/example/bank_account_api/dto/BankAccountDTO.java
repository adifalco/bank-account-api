package com.coding.example.bank_account_api.dto;

import com.coding.example.bank_account_api.domainvalue.AccountType;

public class BankAccountDTO {

    private Integer accountNumber;
    private AccountType accountType;
    private Double balance;

    private BankAccountDTO() {
    }

    private BankAccountDTO(Integer accountNumber, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
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

    private void setBalance(Double balance) {
        this.balance = balance;
    }

    public static BankAccountDTOBuilder newBuilder() {
        return new BankAccountDTOBuilder();
    }

    public static class BankAccountDTOBuilder {

        private Integer accountNumber;
        private AccountType accountType;
        private Double balance;

        public BankAccountDTOBuilder setAccountNumber(Integer accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public BankAccountDTOBuilder setAccountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public BankAccountDTOBuilder setBalance(Double balance) {
            this.balance = balance;
            return this;
        }

        public BankAccountDTO build() {
            BankAccountDTO bankAccountDTO = new BankAccountDTO(accountNumber, accountType);
            bankAccountDTO.setBalance(balance);
            return bankAccountDTO;
        }
    }
}
