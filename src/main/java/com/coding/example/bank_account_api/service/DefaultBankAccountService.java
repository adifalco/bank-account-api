package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.StatementDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.exceptions.NotEnoughFundsException;
import org.springframework.stereotype.Service;

@Service
public class DefaultBankAccountService implements BankAccountService {

    @Override
    public BankAccountDTO create(NewAccountRequestDTO newAccountRequestDTO) throws EntityNotFoundException {
        return null;
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
