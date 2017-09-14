package com.coding.example.bank_account_api.service;

import com.coding.example.bank_account_api.domain.Customer;
import com.coding.example.bank_account_api.dto.CustomerDTO;
import com.coding.example.bank_account_api.exceptions.ConstraintViolationException;
import com.coding.example.bank_account_api.exceptions.EntityNotFoundException;
import com.coding.example.bank_account_api.mapper.CustomerMapper;
import com.coding.example.bank_account_api.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DefaultCustomerService implements CustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerService.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public DefaultCustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Long create(CustomerDTO customerDTO) throws ConstraintViolationException {

        Customer customer = CustomerMapper.makeCustomer(customerDTO);

        try {
            customer = customerRepository.save(customer);
        } catch (DataIntegrityViolationException ex) {
            LOG.error("Duplicated email found on customer creation: " + customer.getEmail());
            throw new ConstraintViolationException(ex.getMessage());
        }

        return customer.getId();
    }

    @Override
    @Transactional
    public CustomerDTO findByEmail(String email) throws EntityNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            String msg = String.format("Email %s not found", email);
            LOG.warn(msg);
            throw new EntityNotFoundException(msg);
        }
        return CustomerMapper.makeCustomerDTO(customer);
    }
}
