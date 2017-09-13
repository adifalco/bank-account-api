package com.coding.example.bank_account_api.rest;

import com.coding.example.bank_account_api.dto.CustomerDTO;
import com.coding.example.bank_account_api.dto.SignUpResponseDTO;
import com.coding.example.bank_account_api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public SignUpResponseDTO signUp(@Valid @RequestBody CustomerDTO customerDTO) {
        Long customerId = customerService.create(customerDTO);
        return SignUpResponseDTO.newBuilder().setCustomerId(customerId).build();
    }

}
