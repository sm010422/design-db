package com.example.designdb.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.designdb.domain.account.DepositAccount;
import com.example.designdb.domain.card.Card;
import com.example.designdb.domain.common.AccountType;
import com.example.designdb.domain.common.TxnType;
import com.example.designdb.domain.customer.Customer;
import com.example.designdb.domain.txn.AccountTransaction;
import com.example.designdb.dto.AccountResponse;
import com.example.designdb.dto.CustomerCreateRequest;
import com.example.designdb.dto.IssueCardRequest;
import com.example.designdb.dto.MoneyRequest;
import com.example.designdb.dto.OpenAccountRequest;
import com.example.designdb.dto.TxnResponse;
import com.example.designdb.repo.AccountTransactionRepository;
import com.example.designdb.repo.CardRepository;
import com.example.designdb.repo.CustomerRepository;
import com.example.designdb.repo.DepositAccountRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankingService {

    private final CustomerRepository customerRepo;
    private final DepositAccountRepository accountRepo;
    private final AccountTransactionRepository txnRepo;
    private final CardRepository cardRepo;

    /* 고객 생성 */
    @Transactional
    public Long createCustomer(CustomerCreateRequest req) {
        Customer c = Customer.builder()
                .rrn(req.rrn())
                .name(req.name())
                .address(req.address())
                .birthDate(req.birthDate())
                .email(req.email())
                .phone(req.phone())
                .job(req.job())
                .build();
        return customerRepo.save(c).getId();
    }

    /* 계좌 개설 */
    @Transactional
    public AccountResponse openAccount(OpenAccountRequest req) {
        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new EntityNotFoundException("고객 없음"));

        DepositAccount a = DepositAccount.builder()
                .customer(customer)
                .accountType(req.accountType() == null ? AccountType.DEMAND : req.accountType())
                .accountNo(req.accountNo())
                .balance(BigDecimal.ZERO)
                .cardRequested(false)
                .openedAt(LocalDate.now(ZoneId.of("UTC")))
                .build();
        a = accountRepo.save(a);
        return new AccountResponse(a.getId(), a.getAccountNo(), customer.getId(),
                a.getAccountType(), a.getBalance(), a.isCardRequested(), a.getOpenedAt());
    }

    /* 입금 */
    @Transactional
    public TxnResponse deposit(MoneyRequest req) {
        DepositAccount acc = accountRepo.findByIdForUpdate(req.accountId())
                .orElseThrow(() -> new EntityNotFoundException("계좌 없음"));

        BigDecimal after = acc.getBalance().add(req.amount());
        int nextNo = txnRepo.currentMaxNo(acc.getId()) + 1;

        AccountTransaction t = AccountTransaction.builder()
                .account(acc)
                .txnNo(nextNo)
                .txnAt(LocalDateTime.now(ZoneId.of("UTC")))
                .txnType(TxnType.DEPOSIT)
                .description(req.description())
                .amount(req.amount())
                .balanceAfter(after)
                .build();
        txnRepo.save(t);

        acc.setBalance(after);
        // JPA가 flush 시점에 업데이트

        return new TxnResponse(t.getId(), t.getTxnNo(), acc.getId(),
                t.getTxnAt(), t.getTxnType(), t.getDescription(), t.getAmount(), t.getBalanceAfter());
    }

    /* 출금 */
    @Transactional
    public TxnResponse withdraw(MoneyRequest req) {
        DepositAccount acc = accountRepo.findByIdForUpdate(req.accountId())
                .orElseThrow(() -> new EntityNotFoundException("계좌 없음"));

        if (acc.getBalance().compareTo(req.amount()) < 0) {
            throw new IllegalArgumentException("잔액 부족");
        }
        BigDecimal after = acc.getBalance().subtract(req.amount());
        int nextNo = txnRepo.currentMaxNo(acc.getId()) + 1;

        AccountTransaction t = AccountTransaction.builder()
                .account(acc)
                .txnNo(nextNo)
                .txnAt(LocalDateTime.now(ZoneId.of("UTC")))
                .txnType(TxnType.WITHDRAW)
                .description(req.description())
                .amount(req.amount())
                .balanceAfter(after)
                .build();
        txnRepo.save(t);

        acc.setBalance(after);

        return new TxnResponse(t.getId(), t.getTxnNo(), acc.getId(),
                t.getTxnAt(), t.getTxnType(), t.getDescription(), t.getAmount(), t.getBalanceAfter());
    }

    /* 최근 거래 20건 */
    @Transactional(readOnly = true)
    public List<TxnResponse> recentTxns(Long accountId) {
        return txnRepo.findTop20ByAccount_IdOrderByTxnAtDesc(accountId).stream()
                .map(t -> new TxnResponse(t.getId(), t.getTxnNo(), t.getAccount().getId(),
                        t.getTxnAt(), t.getTxnType(), t.getDescription(),
                        t.getAmount(), t.getBalanceAfter()))
                .toList();
    }

    /* 카드 발급 */
    @Transactional
    public String issueCard(IssueCardRequest req) {
        Customer c = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new EntityNotFoundException("고객 없음"));
        DepositAccount a = accountRepo.findById(req.accountId())
                .orElseThrow(() -> new EntityNotFoundException("계좌 없음"));

        Card card = Card.builder()
                .cardNo(req.cardNo())
                .customer(c)
                .account(a)
                .appliedAt(req.appliedAt())
                .creditLimit(req.creditLimit())
                .billingDay(req.billingDay())
                .cardType(req.cardType())
                .build();
        cardRepo.save(card);

        a.setCardRequested(true);
        return card.getCardNo();
    }
}
