package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.dto.CustomerDTO;
import com.coding.example.bank_account_api.exceptions.ConstraintViolationException;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;

public interface CustomerService {

    Long create(CustomerDTO customerDTO) throws ConstraintViolationException;

    CustomerDTO findByEmail(String email) throws EntityNotFoundException;
}
