package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.exceptions.NotEnoughFundsException;

public interface BankAccountService {

    BankAccountDTO create(NewAccountRequestDTO newAccountRequestDTO) throws EntityNotFoundException;

    TransactionDTO fundAccount(Integer accountNumber, Double amount) throws EntityNotFoundException;

    TransactionDTO withdraw(Integer accountNumber, Double amount) throws EntityNotFoundException, NotEnoughFundsException;
}
