package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.dto.CustomerDTO;

public interface CustomerService {

    Long create(CustomerDTO customerDTO);
}
