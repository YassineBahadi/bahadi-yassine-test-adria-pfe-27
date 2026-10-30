package com.yassine.accountservice.web.rest;

import com.yassine.accountservice.domain.Account;
import com.yassine.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author pc
 **/
@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable UUID id) {
        return accountService.getAccount(id);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PatchMapping("/{id}/block")
    public Account blockAccount(@PathVariable UUID id) {
        return accountService.blockAccount(id);
    }

    @PatchMapping("/{id}/balance")
    public Account updateBalance(
            @PathVariable UUID id,
            @RequestParam BigDecimal amount) {
        return accountService.updateBalance(id, amount);
    }


}
