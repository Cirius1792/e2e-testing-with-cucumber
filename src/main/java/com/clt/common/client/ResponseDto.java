package com.clt.common.client;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto<T> {
    String status;
    List<ErrorDto> errors;
    T payload;
}
