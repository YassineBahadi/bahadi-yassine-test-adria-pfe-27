package com.yassine.accountservice.repository;

import com.yassine.accountservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author pc
 **/
public interface AccountRepository extends JpaRepository<Account, UUID> {
}
