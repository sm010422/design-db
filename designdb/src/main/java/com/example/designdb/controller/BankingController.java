package com.example.designdb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.designdb.dto.AccountResponse;
import com.example.designdb.dto.CustomerCreateRequest;
import com.example.designdb.dto.IssueCardRequest;
import com.example.designdb.dto.MoneyRequest;
import com.example.designdb.dto.OpenAccountRequest;
import com.example.designdb.dto.TxnResponse;
import com.example.designdb.service.BankingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService service;

    /* 헬스체크 */
    @GetMapping("/health")
    public String health() { return "OK"; }

    /* 고객 생성 */
    @PostMapping("/customers")
    public ResponseEntity<Long> createCustomer(@Valid @RequestBody CustomerCreateRequest req) {
        return ResponseEntity.ok(service.createCustomer(req));
    }

    /* 계좌 개설 */
    @PostMapping("/accounts")
    public ResponseEntity<AccountResponse> openAccount(@Valid @RequestBody OpenAccountRequest req) {
        return ResponseEntity.ok(service.openAccount(req));
    }

    /* 입금 */
    @PostMapping("/accounts/deposit")
    public ResponseEntity<TxnResponse> deposit(@Valid @RequestBody MoneyRequest req) {
        return ResponseEntity.ok(service.deposit(req));
    }

    /* 출금 */
    @PostMapping("/accounts/withdraw")
    public ResponseEntity<TxnResponse> withdraw(@Valid @RequestBody MoneyRequest req) {
        return ResponseEntity.ok(service.withdraw(req));
    }

    /* 최근 거래(20건) */
    @GetMapping("/accounts/{accountId}/txns")
    public ResponseEntity<?> recentTxns(@PathVariable Long accountId) {
        return ResponseEntity.ok(service.recentTxns(accountId));
    }

    /* 카드 발급 */
    @PostMapping("/cards")
    public ResponseEntity<String> issueCard(@Valid @RequestBody IssueCardRequest req) {
        return ResponseEntity.ok(service.issueCard(req));
    }
}
