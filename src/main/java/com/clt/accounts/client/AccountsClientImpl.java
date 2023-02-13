package com.clt.accounts.client;

import java.time.LocalDate;

import com.clt.common.client.ResponseDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import com.clt.accounts.client.dto.BalanceDto;
import com.clt.accounts.client.dto.TransactionDto;
import com.clt.accounts.client.dto.TransactionsDto;
import com.clt.common.router.WebClientErrorFilter;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class AccountsClientImpl implements AccountsClient {

        static final String AUTH_SCHEMA = "S2S";
        final String BALANCE_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/balance";
        final String TRANSACTION_ENDPOINT = "/api/gbs/banking/v4.0/accounts/{accountId}/transactions";


        static final ParameterizedTypeReference<ResponseDto<BalanceDto>> typeReferenceBalanceResponse = new ParameterizedTypeReference<ResponseDto<BalanceDto>>() {};
        static final ParameterizedTypeReference<ResponseDto<TransactionsDto>> typeReferenceTransactionsResponse = new ParameterizedTypeReference<ResponseDto<TransactionsDto>>() {};
        
        final String basePath;
        final WebClient client;

        public AccountsClientImpl(String basePath, String apiKey) {
                this.basePath = basePath;
                this.client = WebClient.builder()
                                .defaultHeader("Api-Key", apiKey)
                                .defaultHeader("Auth-Schema", AUTH_SCHEMA)
                                .filter(WebClientErrorFilter.errorFilter())
                                .baseUrl(this.basePath)
                                .build();
        }

        @Override
        public Mono<BalanceDto> retrieveBalance(Long accountId) {
                return this.client.get()
                                .uri(BALANCE_ENDPOINT, accountId)
                                .retrieve()
                                .bodyToMono(typeReferenceBalanceResponse)
                                .map(el -> el.getPayload());
        }

        @Override
        public Flux<TransactionDto> retrieveTransactions(Long accountId, LocalDate fromAccountingDate,
                        LocalDate toAccountingDate) {
                return this.client.get()
                                .uri(uriBuilder -> uriBuilder.path(TRANSACTION_ENDPOINT)
                                                .queryParam("fromAccountingDate", fromAccountingDate.toString())
                                                .queryParam("toAccountingDate", toAccountingDate.toString())
                                                .build(accountId))
                                .retrieve()
                                .bodyToMono(typeReferenceTransactionsResponse)
                                .map(el -> el.getPayload())
                                .flatMapMany(el -> Flux.fromIterable(el.getList()));
        }

}
