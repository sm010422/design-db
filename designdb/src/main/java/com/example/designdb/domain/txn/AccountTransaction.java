package com.example.designdb.domain.txn;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.designdb.domain.account.DepositAccount;
import com.example.designdb.domain.common.TxnType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_transaction", uniqueConstraints = @UniqueConstraint(name = "uq_txn_account_no", columnNames = {
        "account_id", "txnNo" }), indexes = @Index(name = "ix_txn_account_at", columnList = "account_id, txnAt"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 계좌별 일련번호 */
    @Column(nullable = false)
    private Integer txnNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private DepositAccount account;

    @Column(nullable = false)
    private LocalDateTime txnAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TxnType txnType;

    @Column(length = 200)
    private String description;

    /** 거래금액(+) */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /** 거래 후 잔액(스냅샷) */
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;
}
