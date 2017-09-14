package com.coding.example.bank_account_api.repository;

import com.coding.example.bank_account_api.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findByEmail(String email);
}
