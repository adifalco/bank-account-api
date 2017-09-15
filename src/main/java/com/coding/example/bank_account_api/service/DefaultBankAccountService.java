package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.domain.BankAccount;
import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.StatementDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.exceptions.NotEnoughFundsException;
import com.coding.example.bank_account_api.mapper.BankAccountMapper;
import com.coding.example.bank_account_api.repository.BankAccountRepository;
import com.coding.example.bank_account_api.repository.BankTransactionRepository;
import com.coding.example.bank_account_api.util.BankUtils;
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
        return null;
    }

    @Override
    public TransactionDTO withdraw(Integer accountNumber, Double amount) throws EntityNotFoundException, NotEnoughFundsException {
        return null;
    }

    @Override
    public StatementDTO getStatement(Integer accountNumber) throws EntityNotFoundException {
        return null;
    }
}
