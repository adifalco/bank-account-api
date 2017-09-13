package com.coding.example.bank_account_api.rest;

import com.coding.example.bank_account_api.domainvalue.AccountType;
import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.service.BankAccountService;
import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    private static final Long CUSTOMER_ID = 1L;
    private static final Integer ACCOUNT_NUMBER = 12345678;
    private static final AccountType CURRENT_ACCOUNT = AccountType.CURRENT_ACCOUNT;
    private static final Double BALANCE = 0.00d;

    private static final String ERROR_MESSAGE = "An error message";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankAccountService bankAccountServiceMock;

    private Gson gson = new Gson();

    @Test
    public void create_whenAllCorrect_returnsStatusCreated() throws Exception {
        NewAccountRequestDTO newAccountRequestDTO = buildNewAccountRequestDTO();
        BankAccountDTO bankAccountDTO = buildBankAccountDTO();
        when(bankAccountServiceMock.create(argThat(new NewAccountRequestDTOMatcher(newAccountRequestDTO)))).thenReturn(bankAccountDTO);

        this.mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(newAccountRequestDTO, NewAccountRequestDTO.class)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.accountType").value(CURRENT_ACCOUNT.name()))
                .andExpect(jsonPath("$.balance").value(BALANCE));

        verify(bankAccountServiceMock, times(1)).create(argThat(new NewAccountRequestDTOMatcher(newAccountRequestDTO)));
    }

    @Test
    public void create_whenCustomerIdIsMissing_returnsBadRequest() throws Exception {
        NewAccountRequestDTO newAccountRequestDTO = buildNewAccountRequestDTO(null, CURRENT_ACCOUNT);

        this.mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(newAccountRequestDTO, NewAccountRequestDTO.class)))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(0)).create(any(NewAccountRequestDTO.class));
    }

    @Test
    public void create_whenAccountTypeIsMissing_returnsBadRequest() throws Exception {
        NewAccountRequestDTO newAccountRequestDTO = buildNewAccountRequestDTO(CUSTOMER_ID, null);

        this.mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(newAccountRequestDTO, NewAccountRequestDTO.class)))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(0)).create(any(NewAccountRequestDTO.class));
    }

    @Test
    public void create_whenCustomerNotFound_returnsBadRequest() throws Exception {
        NewAccountRequestDTO newAccountRequestDTO = buildNewAccountRequestDTO(CUSTOMER_ID, CURRENT_ACCOUNT);
        when(bankAccountServiceMock.create(argThat(new NewAccountRequestDTOMatcher(newAccountRequestDTO)))).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        this.mockMvc.perform(post("/api/v1/account")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(newAccountRequestDTO, NewAccountRequestDTO.class)))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(1)).create(argThat(new NewAccountRequestDTOMatcher(newAccountRequestDTO)));
    }

    private BankAccountDTO buildBankAccountDTO() {

        return BankAccountDTO.newBuilder()
                .setAccountNumber(ACCOUNT_NUMBER)
                .setAccountType(CURRENT_ACCOUNT)
                .setBalance(BALANCE).build();
    }

    private NewAccountRequestDTO buildNewAccountRequestDTO() {
        return buildNewAccountRequestDTO(CUSTOMER_ID, CURRENT_ACCOUNT);
    }

    private NewAccountRequestDTO buildNewAccountRequestDTO(Long customerId, AccountType accountType) {

        return NewAccountRequestDTO.newBuilder()
                .setCustomerId(customerId)
                .setAccountType(accountType)
                .build();
    }

    private class NewAccountRequestDTOMatcher extends ArgumentMatcher<NewAccountRequestDTO> {

        private NewAccountRequestDTO expected;

        public NewAccountRequestDTOMatcher(NewAccountRequestDTO newAccountRequestDTO) {
            this.expected = newAccountRequestDTO;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof NewAccountRequestDTO) {
                NewAccountRequestDTO actual = (NewAccountRequestDTO) o;
                return EqualsBuilder.reflectionEquals(expected, actual);
            }
            return false;
        }
    }

}