package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.dto.CustomerDTO;
import com.coding.example.bank_account_api.exceptions.ConstraintViolationException;
import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerService implements CustomerService {

    @Override
    public Long create(CustomerDTO customerDTO) throws ConstraintViolationException{
        throw new ConstraintViolationException("un mensaje");
    }
}
