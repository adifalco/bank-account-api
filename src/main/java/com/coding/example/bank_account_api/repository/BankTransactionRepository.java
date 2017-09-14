package com.coding.example.bank_account_api.repository;

import com.coding.example.bank_account_api.domain.BankTransaction;
import org.springframework.data.repository.CrudRepository;

public interface BankTransactionRepository extends CrudRepository<BankTransaction, Long> {
}
