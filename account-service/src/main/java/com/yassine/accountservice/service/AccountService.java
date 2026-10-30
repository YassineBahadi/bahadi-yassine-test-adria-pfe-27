package com.yassine.accountservice.service;

import com.yassine.accountservice.domain.Account;
import com.yassine.accountservice.domain.AccountStatus;
import com.yassine.accountservice.repository.AccountRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account createAccount(Account account){
        return accountRepository.save(account);
    }

    @Transactional
    public Account getAccount(UUID id){
        return accountRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account with id " + id + " not found"
                ));
    }

    @Transactional
    public List<Account> getAllAccounts(){
        return accountRepository.findAll();
    }

    public Account blockAccount(UUID id){
        Account account = getAccount(id);
        account.setStatus(AccountStatus.BLOCKED);
        return accountRepository.save(account);
    }

    public Account updateBalance(UUID id, BigDecimal newBalance){
        if(newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Balance must be greater than zero");
        }

        Account account=getAccount(id);
        account.setBalance(newBalance);
        return accountRepository.save(account);


    }


    public Account debit(
            UUID id,
            BigDecimal amount,
            String currency) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid amount");
        }

        Account account = getAccount(id);

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Account is blocked");
        }

        if (!account.getCurrency().equalsIgnoreCase(currency)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Currency mismatch");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        return accountRepository.save(account);
    }


}
