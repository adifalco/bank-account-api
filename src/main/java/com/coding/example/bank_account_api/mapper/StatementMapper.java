package com.coding.example.bank_account_api.mapper;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.dto.StatementDTO;

import java.util.Date;
import java.util.stream.Collectors;

public class StatementMapper {

    public static StatementDTO makeStatementDTO(BankAccount bankAccount) {
        return StatementDTO.newBuilder()
                .setFirstName(bankAccount.getCustomer().getFirstName())
                .setLastName(bankAccount.getCustomer().getLastName())
                .setAccountNumber(bankAccount.getAccountNumber())
                .setDate(new Date())
                .setBalance(bankAccount.getBalance())
                .setTransactions(bankAccount.getTransactions().stream().map(BankTransactionMapper::makeTransactionDTO).collect(Collectors.toList()))
                .build();
    }
}
