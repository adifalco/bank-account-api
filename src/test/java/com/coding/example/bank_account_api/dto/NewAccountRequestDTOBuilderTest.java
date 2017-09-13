package com.coding.example.bank_account_api.dto;

import com.coding.example.bank_account_api.domainvalue.AccountType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NewAccountRequestDTOBuilderTest {

    private static final Long ID = 1L;
    private static final AccountType CURRENT_ACCOUNT = AccountType.CURRENT_ACCOUNT;

    @Test
    public void build_returnsNewAccountRequestDTO() throws Exception {
        //Given
        NewAccountRequestDTO.NewAccountRequestDTOBuilder builder = NewAccountRequestDTO.newBuilder();

        //When
        builder.setCustomerId(ID);
        builder.setAccountType(CURRENT_ACCOUNT);
        NewAccountRequestDTO newAccountRequestDTO = builder.build();

        //Then
        assertEquals(ID, newAccountRequestDTO.getCustomerId());
        assertEquals(CURRENT_ACCOUNT, newAccountRequestDTO.getAccountType());
    }

}