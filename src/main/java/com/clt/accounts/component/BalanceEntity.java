package com.clt.accounts.component;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceEntity {
    LocalDate date;
    BigDecimal availableBalance;
    BigDecimal balance;
    String currency;
}
