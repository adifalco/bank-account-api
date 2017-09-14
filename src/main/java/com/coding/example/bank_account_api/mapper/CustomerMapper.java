package com.coding.example.bank_account_api.mapper;

import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.dto.CustomerDTO;

public class CustomerMapper {

    public static Customer makeCustomer(CustomerDTO customerDTO) {
        return new Customer(customerDTO.getFirstName(), customerDTO.getLastName(), customerDTO.getEmail(), customerDTO.getPassword());
    }

    public static CustomerDTO makeCustomerDTO(Customer customer) {
        return CustomerDTO.newBuilder()
                .setFirstName(customer.getFirstName())
                .setLastName(customer.getLastName())
                .setEmail(customer.getEmail())
                .build();
    }
}
