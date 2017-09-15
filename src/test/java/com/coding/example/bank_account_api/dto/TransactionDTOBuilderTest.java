package com.coding.example.bank_account_api.dto;

import com.coding.example.bank_account_api.domainvalue.AccountType;
import com.coding.example.bank_account_api.domainvalue.TransactionType;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TransactionDTOBuilderTest {

    private static final Integer ACCOUNT_NUMBER = 12345678;
    private static final AccountType CURRENT_ACCOUNT = AccountType.CURRENT_ACCOUNT;
    private static final Double BALANCE = 850.25d;
    private static final Double DEPOSIT_AMOUNT = 100.00d;
    private static final TransactionType TRANSACTION_DEPOSIT = TransactionType.DEPOSIT;
    private static final Date DATE = new Date();

    @Test
    public void build_returnsTransactionDTO() throws Exception {
        //Given
        TransactionDTO.TransactionDTOBuilder builder = TransactionDTO.newBuilder();

        //When
        builder.setAccountNumber(ACCOUNT_NUMBER);
        builder.setTransactionType(TRANSACTION_DEPOSIT);
        builder.setAmount(DEPOSIT_AMOUNT);
        builder.setBalance(BALANCE);
        builder.setTransactionDate(DATE);
        TransactionDTO transactionDTO = builder.build();

        //Then
        assertEquals(ACCOUNT_NUMBER, transactionDTO.getAccountNumber());
        assertEquals(TRANSACTION_DEPOSIT, transactionDTO.getTransactionType());
        assertEquals(DEPOSIT_AMOUNT, transactionDTO.getAmount());
        assertEquals(BALANCE, transactionDTO.getBalance());
        assertEquals(DATE, transactionDTO.getTransactionDate());
    }

}