package com.coding.example.bank_account_api.mapper;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.dto.BankAccountDTO;

public class BankAccountMapper {

    public static BankAccountDTO makeBankAccountDTO(BankAccount bankAccount) {
        return BankAccountDTO.newBuilder()
                .setAccountNumber(bankAccount.getAccountNumber())
                .setAccountType(bankAccount.getAccountType())
                .setBalance(bankAccount.getBalance())
                .build();
    }


}
