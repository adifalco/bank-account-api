package com.coding.example.bank_account_api.mapper;

import com.coding.example.bank_account_api.domain.BankTransaction;
import com.coding.example.bank_account_api.dto.TransactionDTO;

public class BankTransactionMapper {

    public static TransactionDTO makeTransactionDTO(BankTransaction bankTransaction) {
        return TransactionDTO.newBuilder()
                .setAccountNumber(bankTransaction.getBankAccount().getAccountNumber())
                .setTransactionType(bankTransaction.getTransactionType())
                .setAmount(bankTransaction.getAmount())
                .setBalance(bankTransaction.getUpdatedBalance())
                .setTransactionDate(bankTransaction.getTransactionDate())
                .build();
    }
}
