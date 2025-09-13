package com.example.designdb.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.designdb.domain.txn.AccountTransaction;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    @Query("select coalesce(max(t.txnNo),0) from AccountTransaction t where t.account.id = :accountId")
    int currentMaxNo(@Param("accountId") Long accountId);

    List<AccountTransaction> findTop20ByAccount_IdOrderByTxnAtDesc(Long accountId);
}
