package com.example.designdb.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.designdb.domain.account.DepositAccount;

import jakarta.persistence.LockModeType;

public interface DepositAccountRepository extends JpaRepository<DepositAccount, Long> {

    /** 계좌 행 잠금(PESSIMISTIC_WRITE)로 동시성 제어 */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from DepositAccount a where a.id = :id")
    Optional<DepositAccount> findByIdForUpdate(@Param("id") Long id);
}
