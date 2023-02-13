package com.clt.accounts.client.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BalanceDto {
    public static String DATE_FORMAT = "yyyy-MM-dd";
    LocalDate date;
    BigDecimal availableBalance;
    BigDecimal balance;
    String currency;

    @Builder
    public BalanceDto(String date, BigDecimal availableBalance, BigDecimal balance, String currency) {
        this.setDate(date);
        this.availableBalance = availableBalance;
        this.balance = balance;
        this.currency = currency;
    }

    void setDate(String date) {
        this.date = LocalDate.parse(date);
    }
}
