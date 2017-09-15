package com.coding.example.bank_account_api.repository;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.domain.BankTransaction;
import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.domainvalue.AccountType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BankAccountRepositoryTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final Long NEW_ACCOUNT_ID = 2L;
    private static final Long CUSTOMER_ID = 1L;
    private static final Long TRANSACTION_ID = 1L;
    private static final Long NEW_TRANSACTION_ID = 2L;
    private static final Integer ACCOUNT_NUMBER = 23456789;
    private static final Double AMOUNT = 50.00d;
    private static final Double NEW_BALANCE = 395.12d;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Transactional
    public void find_returnsBankAccountWithTransaction() {
        //When
        BankAccount bankAccount = bankAccountRepository.findOne(ACCOUNT_ID);

        //Then
        List<BankTransaction> transactions = bankAccount.getTransactions();

        assertNotNull(bankAccount);
        assertEquals(1, transactions.size());
    }

    @Test
    @Transactional
    public void saveNewAccount_returnsNewId() {
        //Given
        Customer customer = customerRepository.findOne(CUSTOMER_ID);
        BankAccount bankAccount = new BankAccount(ACCOUNT_NUMBER, AccountType.CURRENT_ACCOUNT, customer);

        //When
        bankAccount = bankAccountRepository.save(bankAccount);

        //Then
        assertEquals(NEW_ACCOUNT_ID, bankAccount.getId());
    }

}