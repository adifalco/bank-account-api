package com.coding.example.bank_account_api.dto;

import com.coding.example.bank_account_api.domainvalue.AccountType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BankAccountDTOBuilderTest {

    private static final Integer ACCOUNT_NUMBER = 12345678;
    private static final AccountType CURRENT_ACCOUNT = AccountType.CURRENT_ACCOUNT;
    private static final Double BALANCE = 12.34;

    @Test
    public void build() throws Exception {
        //Given
        BankAccountDTO.BankAccountDTOBuilder builder = BankAccountDTO.newBuilder();

        //When
        builder.setAccountNumber(ACCOUNT_NUMBER);
        builder.setAccountType(CURRENT_ACCOUNT);
        builder.setBalance(BALANCE);
        BankAccountDTO bankAccountDTO = builder.build();

        //Then
        assertEquals(ACCOUNT_NUMBER, bankAccountDTO.getAccountNumber());
        assertEquals(CURRENT_ACCOUNT, bankAccountDTO.getAccountType());
        assertEquals(BALANCE, bankAccountDTO.getBalance());
    }

}