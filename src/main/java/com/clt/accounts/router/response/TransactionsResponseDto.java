package com.clt.accounts.router.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsResponseDto {
    List<TransactionResponseDto> transactions;

}
