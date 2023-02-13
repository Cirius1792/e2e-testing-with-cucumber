package com.clt.accounts.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionDto {
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    String transactionId;
    String operationId;
    LocalDate accountingDate;
    LocalDate valueDate;
    BigDecimal amount;
    String currency;
    String description;

    @Builder
    public TransactionDto(String transactionId, String operationId, String accountingDate, String valueDate,
            BigDecimal amount, String currency, String description) {
        this.transactionId = transactionId;
        this.operationId = operationId;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.setValueDate(valueDate);
        this.setAccountingDate(accountingDate);
    }

    void setAccountingDate(String accountingDate) {
        if (StringUtils.isNotBlank(accountingDate)) {
            this.accountingDate = LocalDate.parse(accountingDate);
        }
    }

    void setValueDate(String valueDate) {
        if (StringUtils.isNotBlank(valueDate)) {
            this.valueDate = LocalDate.parse(valueDate);
        }
    }
}
