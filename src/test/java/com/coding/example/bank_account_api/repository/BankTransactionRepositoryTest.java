package com.coding.example.bank_account_api.repository;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.domain.BankTransaction;
import com.coding.example.bank_account_api.domainvalue.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BankTransactionRepositoryTest {

    private static final Long ACCOUNT_ID = 1L;
    private static final Long TRANSACTION_ID = 1L;
    private static final Double AMOUNT = 50.00d;
    private static final Double NEW_BALANCE = 395.12d;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Test
    public void find_returnsExistingTransactionWithBankAccount() {
        //When
        BankTransaction bankTransaction = bankTransactionRepository.findOne(TRANSACTION_ID);

        //Then
        assertNotNull(bankTransaction);
        assertNotNull(bankTransaction.getBankAccount());
        assertEquals(ACCOUNT_ID, bankTransaction.getBankAccount().getId());
    }

    @Test
    @Transactional
    public void savedBankTransaction_thenIsAccessibleFromBankAccount() {
        //Given
        BankAccount bankAccount = bankAccountRepository.findOne(ACCOUNT_ID);
        BankTransaction newBankTransaction = new BankTransaction(TransactionType.DEPOSIT, AMOUNT, bankAccount);

        //When
        bankTransactionRepository.save(newBankTransaction);

        //Then
        assertEquals(2, bankAccount.getTransactions().size());
    }
}