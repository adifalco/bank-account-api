package com.coding.example.bank_account_api.rest;

import com.coding.example.bank_account_api.dto.BankAccountDTO;
import com.coding.example.bank_account_api.dto.NewAccountRequestDTO;
import com.coding.example.bank_account_api.dto.StatementDTO;
import com.coding.example.bank_account_api.dto.TransactionDTO;
import com.coding.example.bank_account_api.service.BankAccountService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(final BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public BankAccountDTO create(@Valid @RequestBody NewAccountRequestDTO newAccountRequestDTO) throws Exception {
        return bankAccountService.create(newAccountRequestDTO);
    }

    @PutMapping(path = "/{accountNumber}/deposit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TransactionDTO fundAccount(@PathVariable Integer accountNumber, @RequestParam Double amount) throws Exception {
        Preconditions.checkArgument(amount > 0, "Invalid amount");
        return bankAccountService.fundAccount(accountNumber, amount);
    }

    @PutMapping(path = "/{accountNumber}/withdraw", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TransactionDTO withdraw(@PathVariable Integer accountNumber, @RequestParam Double amount) throws Exception {
        Preconditions.checkArgument(amount > 0, "Invalid amount");
        return bankAccountService.withdraw(accountNumber, amount);
    }

    @GetMapping(path = "/{accountNumber}/statement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public StatementDTO getStatement(@PathVariable Integer accountNumber) throws Exception {
        return bankAccountService.getStatement(accountNumber);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleArgumentException(HttpServletResponse res, IllegalArgumentException ex) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
