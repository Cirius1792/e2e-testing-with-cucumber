package com.clt.accounts.router.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.clt.accounts.component.TransactionEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDto {
    String transactionId;
    String operationId;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate accountingDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate valueDate;
    BigDecimal amount;
    String currency;
    String description;

    public static TransactionResponseDto toDto(TransactionEntity transaction){
        return TransactionResponseDto.builder()
                .transactionId(transaction.getTransactionId())
                .operationId(transaction.getOperationId())
                .accountingDate(transaction.getAccountingDate())
                .valueDate(transaction.getValueDate())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .description(transaction.getDescription())
                .build();
    }
    
}
