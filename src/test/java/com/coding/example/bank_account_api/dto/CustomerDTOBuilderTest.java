package com.coding.example.bank_account_api.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerDTOBuilderTest {

    private static final Long ID = 1L;
    private static final String EMAIL = "anEmail";
    private static final String PASSWORD = "aPassword";

    @Test
    public void build_returnsCustomerDTO() throws Exception {
        //Given
        CustomerDTO.CustomerDTOBuilder builder = CustomerDTO.newBuilder();

        //When
        builder.setId(ID);
        builder.setEmail(EMAIL);
        builder.setPassword(PASSWORD);
        CustomerDTO customerDTO = builder.build();

        //Then
        assertEquals(ID, customerDTO.getId());
        assertEquals(EMAIL, customerDTO.getEmail());
        assertEquals(PASSWORD, customerDTO.getPassword());
    }

}