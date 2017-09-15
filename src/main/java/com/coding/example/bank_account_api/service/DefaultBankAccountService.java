package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.domain.BankTransaction;
import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.domainvalue.TransactionType;
import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.StatementDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.exceptions.NotEnoughFundsException;
import com.coding.example.bank_account_api.mapper.BankAccountMapper;
import com.coding.example.bank_account_api.mapper.BankTransactionMapper;
import com.coding.example.bank_account_api.mapper.StatementMapper;
import com.coding.example.bank_account_api.repository.BankAccountRepository;
import com.coding.example.bank_account_api.repository.BankTransactionRepository;
import com.coding.example.bank_account_api.util.BankUtils;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultBankAccountService implements BankAccountService {

    private BankTransactionRepository bankTransactionRepository;
    private BankAccountRepository bankAccountRepository;
    private InternalCustomerService customerService;

    @Autowired
    public DefaultBankAccountService(BankTransactionRepository bankTransactionRepository, BankAccountRepository bankAccountRepository, InternalCustomerService customerService) {
        this.bankTransactionRepository = bankTransactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.customerService = customerService;
    }

    @Override
    public BankAccountDTO create(NewAccountRequestDTO newAccountRequestDTO) throws EntityNotFoundException {
        Integer accountNumber = newAccountNumber();
        Customer customer = customerService.findCustomerChecked(newAccountRequestDTO.getCustomerId());
        BankAccount bankAccount = new BankAccount(accountNumber, newAccountRequestDTO.getAccountType(), customer);
        bankAccountRepository.save(bankAccount);
        return BankAccountMapper.makeBankAccountDTO(bankAccount);
    }

    private Integer newAccountNumber() {
        return BankUtils.randomInt(8);
    }

    @Override
    public TransactionDTO fundAccount(Integer accountNumber, Double amount) throws EntityNotFoundException {
        Preconditions.checkNotNull(amount, "Amount cannot be null");
        Preconditions.checkArgument(amount > 0, "Invalid amount");

        BankAccount bankAccount = findBankAcountChecked(accountNumber);
        return addNewBankTransaction(TransactionType.DEPOSIT, amount, bankAccount);
    }

    @Override
    public TransactionDTO withdraw(Integer accountNumber, Double amount) throws EntityNotFoundException, NotEnoughFundsException {
        Preconditions.checkNotNull(amount, "Amount cannot be null");
        Preconditions.checkArgument(amount > 0, "Invalid amount");

        BankAccount bankAccount = findBankAcountChecked(accountNumber);
        checkEnoughFunds(bankAccount, amount);
        return addNewBankTransaction(TransactionType.WITHDRAWAL, amount * -1, bankAccount);
    }

    private TransactionDTO addNewBankTransaction(TransactionType transactionType, Double amount, BankAccount bankAccount) {
        bankAccount.add(amount);
        BankTransaction bankTransaction = new BankTransaction(transactionType, amount, bankAccount);
        bankTransactionRepository.save(bankTransaction);
        return BankTransactionMapper.makeTransactionDTO(bankTransaction);
    }

    private void checkEnoughFunds(BankAccount bankAccount, Double amountToWithdraw) throws NotEnoughFundsException {
        if (amountToWithdraw.compareTo(bankAccount.getBalance()) > 0) {
            throw new NotEnoughFundsException("Not enough funds to witheraw this amount of money");
        }
    }

    @Override
    public StatementDTO getStatement(Integer accountNumber) throws EntityNotFoundException {
        BankAccount bankAccount = findBankAcountChecked(accountNumber);
        return StatementMapper.makeStatementDTO(bankAccount);
    }

    private BankAccount findBankAcountChecked(Integer accountNumber) throws EntityNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber);
        if (bankAccount == null) {
            throw new EntityNotFoundException("Account number not found: " + accountNumber);
        }
        return bankAccount;
    }

}
