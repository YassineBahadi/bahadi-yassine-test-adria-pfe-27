package com.yassine.transactionservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author pc
 **/
public record AccountResponse(
        UUID id,
        String customerReference,
        String status,
        String currency,
        BigDecimal balance
) {
}
