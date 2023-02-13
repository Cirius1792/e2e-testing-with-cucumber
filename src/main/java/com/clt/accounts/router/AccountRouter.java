package com.clt.accounts.router;

import com.clt.accounts.component.AccountComponent;
import com.clt.accounts.router.response.BalanceResponseDto;
import com.clt.accounts.router.response.TransactionResponseDto;
import com.clt.accounts.router.response.TransactionsResponseDto;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;

public class AccountRouter {
    static final String TRANSACTIONS_DATE_TO_PARAM = "dateTo";
    static final String TRANSACTIONS_DATE_FROM_PARAM = "dateFrom";
    final AccountComponent accountService;
    final Clock clock;

    public AccountRouter(AccountComponent accountService, Clock clock) {
        this.accountService = accountService;
        this.clock = clock;
    }

    Mono<ServerResponse> getBalance(ServerRequest request) {
        return ServerResponse.ok()
                .body(accountService.retrieveBalance()
                                .map(BalanceResponseDto::toDto),
                        BalanceResponseDto.class);
    }

    Mono<ServerResponse> getTransactions(ServerRequest request) {
        LocalDate to = request.queryParam(TRANSACTIONS_DATE_TO_PARAM).map(LocalDate::parse).orElse(LocalDate.now(clock));
        LocalDate from = request.queryParam(TRANSACTIONS_DATE_FROM_PARAM).map(LocalDate::parse).orElse(to);
        return ServerResponse.ok()
                .body(accountService.retrieveTransactions(from, to)
                                .log()
                                .map(TransactionResponseDto::toDto)
                                .collect(Collectors.toList())
                                .map(transactions -> TransactionsResponseDto.builder()
                                        .transactions(transactions).build()),
                        TransactionsResponseDto.class);
    }

    public RouterFunction<ServerResponse> accountApis() {
        return route()
                .GET("/balance", this::getBalance,
                        ops -> ops.operationId("getBalance")
                                .tag("Account")
                                .response(responseBuilder().responseCode("200").implementation(BalanceResponseDto.class)))
                .GET("/transactions", this::getTransactions,
                        ops -> ops.operationId("getTransactions")
                                .tag("Account")
                                .description("Retrieves the transactions for the given period. If no dates are " +
                                        "specified, the transactions performed in the current day are returned by default")
                                .parameter(parameterBuilder()
                                        .name(TRANSACTIONS_DATE_FROM_PARAM)
                                        .description("Start date of the range to be used to retrieve the transactions")
                                        .required(false)
                                        .example("2021-12-23"))
                                .parameter(parameterBuilder()
                                        .name(TRANSACTIONS_DATE_TO_PARAM)
                                        .description("End date of the range to be used to retrieve the transactions")
                                        .required(false)
                                        .example("2021-12-26"))
                                .response(responseBuilder().responseCode("200").implementation(TransactionsResponseDto.class)))
                .build();
    }

}
