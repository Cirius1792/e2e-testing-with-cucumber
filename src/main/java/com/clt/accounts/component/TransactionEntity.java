package com.clt.accounts.component;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionEntity {
    String transactionId;
    String operationId;
    LocalDate accountingDate;
    LocalDate valueDate;
    BigDecimal amount;
    String currency;
    String description;
}
