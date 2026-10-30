package com.yassine.transactionservice.client;

import com.yassine.transactionservice.dto.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author pc
 **/
@FeignClient(
        name = "account-service",
        url = "${services.account.url}"
)
public interface AccountClient {

    @GetMapping("/api/accounts/{id}")
    AccountResponse getAccount(@PathVariable("id") UUID id);

    @PatchMapping("/api/accounts/{id}/debit")
    AccountResponse debit(
            @PathVariable("id") UUID id,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("currency") String currency
    );
}