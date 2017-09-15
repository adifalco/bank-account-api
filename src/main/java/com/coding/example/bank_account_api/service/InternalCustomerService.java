package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;

interface InternalCustomerService {

    Customer findCustomerChecked(Long id) throws EntityNotFoundException;
}
