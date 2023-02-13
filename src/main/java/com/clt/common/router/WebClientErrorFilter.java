package com.clt.common.router;

import java.util.List;
import java.util.stream.Collectors;

import com.clt.common.client.ErrorDto;
import com.clt.common.client.ResponseDto;
import com.clt.common.error.ExternalServiceError;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class WebClientErrorFilter {

    static final String DEFAULT_ERROR_CODE = "XX500";
    static final String DEFAULT_ERROR_DESC = "External Service Error";

    public static ExchangeFilterFunction errorFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(WebClientErrorFilter::handleErrors);
    }

    protected static Mono<ClientResponse> handleErrors(ClientResponse response) {
        if (response.statusCode() != null
                && (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError())) {
            return response.bodyToMono(ResponseDto.class)
                    .flatMap(body -> {
                        String errorCode = DEFAULT_ERROR_CODE;
                        String errorDesc = DEFAULT_ERROR_DESC;
                        if (body.getErrors()!= null && !body.getErrors().isEmpty()) {
                            List<ErrorDto> errorDtos = (List<ErrorDto>) body.getErrors();
                            errorCode = errorDtos.stream().map(ErrorDto::getCode)
                                    .collect(Collectors.joining(";"));
                            errorDesc = errorDtos.stream().map(ErrorDto::getDescription)
                                    .collect(Collectors.joining(";"));
                        }
                        log.debug("Error Code is {}", errorCode);
                        return Mono.error(
                                new ExternalServiceError(errorCode, errorDesc, response.rawStatusCode()));
                    });
        } else {
            return Mono.just(response);
        }
    }
}
