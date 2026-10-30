package com.yassine.accountservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author pc
 **/
@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String customerReference;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String currency;
    private BigDecimal balance;
}
