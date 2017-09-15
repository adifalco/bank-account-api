package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.domain.BankTransaction;
import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.domainvalue.AccountType;
import com.coding.example.bank_account_api.domainvalue.TransactionType;
import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.repository.BankAccountRepository;
import com.coding.example.bank_account_api.repository.BankTransactionRepository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBankAccountServiceTest {

    private static final Long CUSTOMER_ID = 1L;
    private static final Long ACCOUNT_ID = 2L;
    private static final Integer ACCOUNT_NUMBER = 12345678;
    private static final Double ZERO_AMOUNT = 0.00d;
    private static final Double BALANCE = new Double(100.01);
    private static final Double DEPOSIT_TRANSACTION_AMOUNT = new Double(100.01);
    private static final Double NEW_TRANSACTION_AMOUNT = new Double(45.67);
    private static final Double BALANCE_AFTER_DEPOSIT = new Double(145.68);
    private static final Double BALANCE_AFTER_WITHDRAWAL = new Double(54.34);

    @Mock
    private BankTransactionRepository bankTransactionRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private InternalCustomerService customerService;
    @InjectMocks
    private DefaultBankAccountService bankAccountService;

    @Test
    public void create_whenAllCorrect_returnsNewBankAccount() throws Exception {
        //Given
        NewAccountRequestDTO accountRequestDTO = buildNewAccountRequestDTO();

        Customer customer = mock(Customer.class);
        when(customerService.findCustomerChecked(CUSTOMER_ID)).thenReturn(customer);
        BankAccount newBankAccount = buildBankAccount(customer);

        Answer bankAccountWithId = buildBankAccountSavingAnswer();
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(bankAccountWithId);

        //When
        BankAccountDTO bankAccountDTO = bankAccountService.create(accountRequestDTO);

        //Then
        assertEquals(ACCOUNT_NUMBER, bankAccountDTO.getAccountNumber());
        assertEquals(AccountType.CURRENT_ACCOUNT, bankAccountDTO.getAccountType());
        assertEquals(ZERO_AMOUNT, bankAccountDTO.getBalance());

        verify(customerService, times(1)).findCustomerChecked(CUSTOMER_ID);
        verify(bankAccountRepository, times(1)).save(argThat(new BankAccountMatcher(newBankAccount)));
    }

    @Test
    public void fundAccount_whenAllCorrect_returnsTransactionDTO() throws Exception {
        //Given
        BankAccount bankAccount = buildBankAccount(mock(Customer.class));
        bankAccount.add(DEPOSIT_TRANSACTION_AMOUNT);
        BankTransaction transaction = new BankTransaction(TransactionType.DEPOSIT, DEPOSIT_TRANSACTION_AMOUNT, bankAccount);
        bankAccount.addBankTransaction(transaction);
        when(bankAccountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(bankAccount);

        //When
        TransactionDTO transactionDTO = bankAccountService.fundAccount(ACCOUNT_NUMBER, NEW_TRANSACTION_AMOUNT);

        //Then
        assertEquals(ACCOUNT_NUMBER, transactionDTO.getAccountNumber());
        assertEquals(TransactionType.DEPOSIT, transactionDTO.getTransactionType());
        assertEquals(BALANCE_AFTER_DEPOSIT, transactionDTO.getBalance());
        assertNotNull(transactionDTO.getTransactionDate());

        BankTransaction expectedTransaction = new BankTransaction(TransactionType.DEPOSIT, NEW_TRANSACTION_AMOUNT, bankAccount);
        verify(bankTransactionRepository, times(1)).save(argThat(new BankTransactionMatcher(expectedTransaction)));
    }

    @Test
    public void withdraw_whenAllCorrect_returnsTransactionDTO() throws Exception {
        //Given
        BankAccount bankAccount = buildBankAccount(mock(Customer.class));
        bankAccount.add(DEPOSIT_TRANSACTION_AMOUNT);
        BankTransaction transaction = new BankTransaction(TransactionType.DEPOSIT, DEPOSIT_TRANSACTION_AMOUNT, bankAccount);
        bankAccount.addBankTransaction(transaction);
        when(bankAccountRepository.findByAccountNumber(ACCOUNT_NUMBER)).thenReturn(bankAccount);

        //When
        TransactionDTO transactionDTO = bankAccountService.withdraw(ACCOUNT_NUMBER, NEW_TRANSACTION_AMOUNT);

        //Then
        assertEquals(ACCOUNT_NUMBER, transactionDTO.getAccountNumber());
        assertEquals(TransactionType.WITHDRAWAL, transactionDTO.getTransactionType());
        assertEquals(BALANCE_AFTER_WITHDRAWAL, transactionDTO.getBalance());
        assertNotNull(transactionDTO.getTransactionDate());

        BankTransaction expectedTransaction = new BankTransaction(TransactionType.WITHDRAWAL, NEW_TRANSACTION_AMOUNT * -1, bankAccount);
        verify(bankTransactionRepository, times(1)).save(argThat(new BankTransactionMatcher(expectedTransaction)));
    }

    private Answer buildBankAccountSavingAnswer() {
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                BankAccount newBankAccount = (BankAccount) invocationOnMock.getArguments()[0];
                ReflectionTestUtils.setField(newBankAccount, "id", ACCOUNT_ID);
                ReflectionTestUtils.setField(newBankAccount, "accountNumber", ACCOUNT_NUMBER);
                return newBankAccount;
            }
        };
    }

    private NewAccountRequestDTO buildNewAccountRequestDTO() {
        return NewAccountRequestDTO.newBuilder()
                .setCustomerId(CUSTOMER_ID)
                .setAccountType(AccountType.CURRENT_ACCOUNT)
                .build();
    }


    private BankAccount buildBankAccount(Customer customer) {
        return new BankAccount(ACCOUNT_NUMBER, AccountType.CURRENT_ACCOUNT, customer);
    }

    private class BankTransactionMatcher extends ArgumentMatcher<BankTransaction> {

        private BankTransaction expected;

        public BankTransactionMatcher(BankTransaction expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof BankTransaction) {
                BankTransaction actual = (BankTransaction) o;
                return EqualsBuilder.reflectionEquals(expected, actual, "transactionDate");
            }
            return false;
        }
    }

    private class BankAccountMatcher extends ArgumentMatcher<BankAccount> {

        private BankAccount expected;

        public BankAccountMatcher(BankAccount expected) {
            this.expected = expected;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof BankAccount) {
                BankAccount actual = (BankAccount) o;

                return actual.getAccountNumber() != null
                        && expected.getCustomer() == actual.getCustomer()
                        && expected.getAccountType() == actual.getAccountType()
                        && expected.getBalance().equals(actual.getBalance());
            }
            return false;
        }
    }

}