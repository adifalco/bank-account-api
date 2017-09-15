package com.coding.example.bank_account_api.repository;

import com.coding.example.bank_account_api.domain.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {

    BankAccount findByAccountNumber(Integer accountNumber);
}
