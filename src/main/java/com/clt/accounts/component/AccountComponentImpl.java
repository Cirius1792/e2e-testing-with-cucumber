package com.clt.accounts.component;

import java.time.LocalDate;

import com.clt.accounts.client.AccountsClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AccountComponentImpl implements AccountComponent {

    final AccountsClient accountsClient;
    final Long accountNumber;

    public AccountComponentImpl(Long accountNumber, AccountsClient accountsClient) {
        if (accountNumber == null)
            throw new IllegalArgumentException("Account number can't be null");
        this.accountNumber = accountNumber;
        this.accountsClient = accountsClient;
    }

    @Override
    public Mono<BalanceEntity> retrieveBalance() {
        return this.accountsClient.retrieveBalance(this.accountNumber)
                .map(balance -> BalanceEntity.builder()
                        .availableBalance(balance.getAvailableBalance())
                        .balance(balance.getBalance())
                        .currency(balance.getCurrency())
                        .date(balance.getDate())
                        .build());
    }

    @Override
    public Flux<TransactionEntity> retrieveTransactions(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null || toDate.isBefore(fromDate))
            return Flux.error(new IllegalArgumentException("Invalid dates"));
        return this.accountsClient.retrieveTransactions(this.accountNumber, fromDate, toDate)
                .map(transaction -> TransactionEntity.builder()
                        .transactionId(transaction.getTransactionId())
                        .operationId(transaction.getOperationId())
                        .valueDate(transaction.getValueDate())
                        .accountingDate(transaction.getAccountingDate())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .description(transaction.getDescription())
                        .build());
    }

}
