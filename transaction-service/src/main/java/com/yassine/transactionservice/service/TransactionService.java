package com.yassine.transactionservice.service;

import com.yassine.transactionservice.client.AccountClient;
import com.yassine.transactionservice.domain.Transaction;
import com.yassine.transactionservice.dto.AccountResponse;
import com.yassine.transactionservice.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author pc
 **/
@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    public TransactionService(
            TransactionRepository transactionRepository ,AccountClient accountClient) {
        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
    }

    public Transaction createTransaction(Transaction transaction) {

        if (transaction.getAmount() == null ||
                transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid amount");
        }

        AccountResponse account =
                accountClient.getAccount(transaction.getAccountId());

        if (!"ACTIVE".equals(account.status())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Account is blocked");
        }

        if (!account.currency().equalsIgnoreCase(
                transaction.getCurrency())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Currency mismatch");
        }

        if (account.balance().compareTo(transaction.getAmount()) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        accountClient.debit(
                account.id(),
                transaction.getAmount(),
                transaction.getCurrency()
        );

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Transaction not found"
                ));
    }

    @Transactional
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public List<Transaction> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
}
