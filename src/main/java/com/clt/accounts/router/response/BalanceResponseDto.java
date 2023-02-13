package com.clt.accounts.router.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.clt.accounts.component.BalanceEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponseDto {
    @JsonFormat(pattern="yyyy-MM-dd")
    LocalDate date;
    BigDecimal availableBalance;
    BigDecimal balance;
    String currency;

    public static BalanceResponseDto toDto(BalanceEntity balance){
        return BalanceResponseDto.builder()
                .date(balance.getDate())
                .availableBalance(balance.getAvailableBalance())
                .balance(balance.getBalance())
                .currency(balance.getCurrency())
                .build();
    }
}
