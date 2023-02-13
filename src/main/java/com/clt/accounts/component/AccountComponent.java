package com.clt.accounts.component;

import java.time.LocalDate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountComponent {

    Mono<BalanceEntity> retrieveBalance();
    Flux<TransactionEntity> retrieveTransactions(LocalDate fromDate, LocalDate toDate);
}
