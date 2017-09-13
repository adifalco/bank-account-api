package com.coding.example.bank_account_api.dto;

import com.coding.example.bank_account_api.domainvalue.AccountType;

import javax.validation.constraints.NotNull;

public class NewAccountRequestDTO {

    @NotNull
    private Long customerId;
    @NotNull
    private AccountType accountType;

    private NewAccountRequestDTO() {
    }


    private NewAccountRequestDTO(Long customerId, AccountType accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public static NewAccountRequestDTOBuilder newBuilder() {
        return new NewAccountRequestDTOBuilder();
    }

    public static class NewAccountRequestDTOBuilder {

        private Long customerId;
        private AccountType accountType;

        public NewAccountRequestDTOBuilder setCustomerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public NewAccountRequestDTOBuilder setAccountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public NewAccountRequestDTO build() {
            return new NewAccountRequestDTO(customerId, accountType);
        }
    }
}
