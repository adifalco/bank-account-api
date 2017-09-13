package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultBankAccountService implements BankAccountService {

    @Override
    public BankAccountDTO create(NewAccountRequestDTO newAccountRequestDTO) throws EntityNotFoundException {
        return null;
    }
}
