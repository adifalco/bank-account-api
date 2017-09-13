package com.coding.example.bank_account_api.dto;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpResponseDTOBuilderTest {

    private static final Long CUSTOMER_ID = 1L;

    @Test
    public void build_returnsSignUpResponseDTO() throws Exception {
        //Given
        SignUpResponseDTO.SignUpResponseDTOBuilder builder = SignUpResponseDTO.newBuilder();

        //When
        builder.setCustomerId(CUSTOMER_ID);
        SignUpResponseDTO signUpResponseDTO = builder.build();

        //Then
        assertEquals(CUSTOMER_ID, signUpResponseDTO.getCustomerId());
    }

}