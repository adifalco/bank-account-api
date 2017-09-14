package com.coding.example.bank_account_api.rest;

import com.coding.example.bank_account_api.domainvalue.AccountType;
import com.coding.example.bank_account_api.domainvalue.TransactionType;
import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.StatementDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.exceptions.NotEnoughFundsException;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    private static final Long CUSTOMER_ID = 1L;
    private static final Integer ACCOUNT_NUMBER = 12345678;
    private static final AccountType CURRENT_ACCOUNT = AccountType.CURRENT_ACCOUNT;
    private static final Double ZERO_BALANCE = 0.00d;
    private static final Double BALANCE = 850.25d;
    private static final Double TRANSACTION_AMOUNT = 100.00d;
    private static final Double ZERO_AMOUNT = 0.00d;
    private static final TransactionType TRANSACTION_DEPOSIT = TransactionType.DEPOSIT;
    private static final TransactionType TRANSACTION_WITHDRAWAL = TransactionType.WITHDRAWAL;
    private static final String FIRST_NAME = "aName";
    private static final String LAST_NAME = "aLastName";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final String DATE_STRING_CEST = "2017-09-14T11:45:01+0200";
    private static final String DATE_STRING_UTC = "2017-09-14T09:45:01+0000";

    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

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
                .andExpect(jsonPath("$.balance").value(ZERO_BALANCE));

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

    @Test
    public void fundAccount_whenAllCorrect_returnsTransactionInformation() throws Exception {
        TransactionDTO transactionDTO = buildTransactionDTO(TRANSACTION_DEPOSIT);
        when(bankAccountServiceMock.fundAccount(ACCOUNT_NUMBER, TRANSACTION_AMOUNT)).thenReturn(transactionDTO);

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/deposit", ACCOUNT_NUMBER).param("amount", TRANSACTION_AMOUNT.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.transactionType").value(TRANSACTION_DEPOSIT.name()))
                .andExpect(jsonPath("$.amount").value(TRANSACTION_AMOUNT))
                .andExpect(jsonPath("$.balance").value(BALANCE))
                .andExpect(jsonPath("$.date").value(DATE_STRING_UTC));


        verify(bankAccountServiceMock, times(1)).fundAccount(ACCOUNT_NUMBER, TRANSACTION_AMOUNT);

    }

    @Test
    public void fundAccount_whenAccountNotFound_returnsBadRequest() throws Exception {
        when(bankAccountServiceMock.fundAccount(ACCOUNT_NUMBER, TRANSACTION_AMOUNT)).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/deposit", ACCOUNT_NUMBER).param("amount", TRANSACTION_AMOUNT.toString()))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(1)).fundAccount(ACCOUNT_NUMBER, TRANSACTION_AMOUNT);

    }

    @Test
    public void fundAccount_whenAmountIsZero_returnsBadRequest() throws Exception {

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/deposit", ACCOUNT_NUMBER).param("amount", ZERO_AMOUNT.toString()))
                .andExpect(status().isBadRequest());


        verify(bankAccountServiceMock, times(0)).fundAccount(anyInt(), anyDouble());

    }

    @Test
    public void withdraw_whenAllCorrect_returnsTransactionInformation() throws Exception {
        TransactionDTO transactionDTO = buildTransactionDTO(TRANSACTION_WITHDRAWAL);
        when(bankAccountServiceMock.withdraw(ACCOUNT_NUMBER, TRANSACTION_AMOUNT)).thenReturn(transactionDTO);

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/withdraw", ACCOUNT_NUMBER).param("amount", TRANSACTION_AMOUNT.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.transactionType").value(TRANSACTION_WITHDRAWAL.name()))
                .andExpect(jsonPath("$.amount").value(TRANSACTION_AMOUNT))
                .andExpect(jsonPath("$.balance").value(BALANCE))
                .andExpect(jsonPath("$.date").value(DATE_STRING_UTC));


        verify(bankAccountServiceMock, times(1)).withdraw(ACCOUNT_NUMBER, TRANSACTION_AMOUNT);

    }

    @Test
    public void withdraw_whenAccountNotFound_returnsBadRequest() throws Exception {
        when(bankAccountServiceMock.withdraw(ACCOUNT_NUMBER, TRANSACTION_AMOUNT)).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/withdraw", ACCOUNT_NUMBER).param("amount", TRANSACTION_AMOUNT.toString()))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(1)).withdraw(ACCOUNT_NUMBER, TRANSACTION_AMOUNT);

    }

    @Test
    public void withdraw_whenNotEnoughFunds_returnsBadRequest() throws Exception {
        when(bankAccountServiceMock.withdraw(ACCOUNT_NUMBER, TRANSACTION_AMOUNT)).thenThrow(new NotEnoughFundsException(ERROR_MESSAGE));

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/withdraw", ACCOUNT_NUMBER).param("amount", TRANSACTION_AMOUNT.toString()))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(1)).withdraw(ACCOUNT_NUMBER, TRANSACTION_AMOUNT);

    }

    @Test
    public void withdraw_whenAmountIsZero_returnsBadRequest() throws Exception {

        this.mockMvc.perform(put("/api/v1/account/{accountNumber}/withdraw", ACCOUNT_NUMBER).param("amount", ZERO_AMOUNT.toString()))
                .andExpect(status().isBadRequest());


        verify(bankAccountServiceMock, times(0)).withdraw(anyInt(), anyDouble());

    }

    @Test
    public void getStatement_returnsSuccessfulResponse() throws Exception {
        StatementDTO statementDTO = buildStatementDTO();
        when(bankAccountServiceMock.getStatement(ACCOUNT_NUMBER)).thenReturn(statementDTO);

        this.mockMvc.perform(get("/api/v1/account/{accountNumber}/statement", ACCOUNT_NUMBER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(LAST_NAME))
                .andExpect(jsonPath("$.accountNumber").value(ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.date").value(DATE_STRING_UTC))
                .andExpect(jsonPath("$.balance").value(BALANCE))
                .andExpect(jsonPath("$.transactions[0].transactionType").value(TRANSACTION_DEPOSIT.name()))
                .andExpect(jsonPath("$.transactions[0].amount").value(TRANSACTION_AMOUNT))
                .andExpect(jsonPath("$.transactions[0].balance").value(BALANCE))
                .andExpect(jsonPath("$.transactions[0].date").value(DATE_STRING_UTC));

        verify(bankAccountServiceMock, times(1)).getStatement(ACCOUNT_NUMBER);
    }

    @Test
    public void getStatement_whenAccountNotFound_returnsBadRequest() throws Exception {
        when(bankAccountServiceMock.getStatement(ACCOUNT_NUMBER)).thenThrow(new EntityNotFoundException(ERROR_MESSAGE));

        this.mockMvc.perform(get("/api/v1/account/{accountNumber}/statement", ACCOUNT_NUMBER))
                .andExpect(status().isBadRequest());

        verify(bankAccountServiceMock, times(1)).getStatement(ACCOUNT_NUMBER);
    }

    private StatementDTO buildStatementDTO() throws Exception {
        return StatementDTO.newBuilder()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setAccountNumber(ACCOUNT_NUMBER)
                .setDate(dateFormat.parse(DATE_STRING_CEST))
                .setBalance(BALANCE)
                .setTransactions(Collections.singletonList(buildTransactionDTO(TRANSACTION_DEPOSIT)))
                .build();
    }

    private TransactionDTO buildTransactionDTO(TransactionType transactionType) throws Exception {
        return TransactionDTO.newBuilder()
                .setAccountNumber(ACCOUNT_NUMBER)
                .setTransactionType(transactionType)
                .setAmount(TRANSACTION_AMOUNT)
                .setBalance(BALANCE)
                .setDate(dateFormat.parse(DATE_STRING_CEST))
                .build();
    }

    private BankAccountDTO buildBankAccountDTO() {

        return BankAccountDTO.newBuilder()
                .setAccountNumber(ACCOUNT_NUMBER)
                .setAccountType(CURRENT_ACCOUNT)
                .setBalance(ZERO_BALANCE).build();
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