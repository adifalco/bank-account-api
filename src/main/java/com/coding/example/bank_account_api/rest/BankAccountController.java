package com.coding.example.bank_account_api.rest;

import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(final BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BankAccountDTO create(@Valid @RequestBody NewAccountRequestDTO newAccountRequestDTO) throws Exception {
        return bankAccountService.create(newAccountRequestDTO);
    }
}
