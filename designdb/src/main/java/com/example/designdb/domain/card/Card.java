package com.example.designdb.domain.card;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.designdb.domain.account.DepositAccount;
import com.example.designdb.domain.common.CardType;
import com.example.designdb.domain.customer.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card", indexes = {
        @Index(name = "ix_card_customer", columnList = "customer_id"),
        @Index(name = "ix_card_account", columnList = "account_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    /** 카드 번호를 PK로 */
    @Id
    @Column(length = 32)
    private String cardNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private DepositAccount account;

    @Column(nullable = false)
    private LocalDate appliedAt;

    /** 체크카드면 null 가능 */
    @Column(precision = 15, scale = 2)
    private BigDecimal creditLimit;

    /** 1~28 권장, null 허용(체크카드) */
    private Integer billingDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CardType cardType;
}
