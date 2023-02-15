package com.clt.userprofile.router;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.clt.userprofile.error.UserProfileException;
import com.clt.userprofile.error.UserProfileNotFoundException;

import lombok.extern.slf4j.Slf4j;

record ExceptionRule(Class<?> exceptionClass, HttpStatus status) {
}

@Component
@Slf4j
public class GlobalErrorAttributes extends DefaultErrorAttributes {
        final static String INTERNAL_SERVER_ERROR_MESSAGE = "An error occurred while serving the request";

        private final List<ExceptionRule> exceptionsRules = List.of(
                        new ExceptionRule(UserProfileException.class, HttpStatus.BAD_REQUEST),
                        new ExceptionRule(UserProfileNotFoundException.class, HttpStatus.NOT_FOUND)
                        );
        @Override
        public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
                Throwable error = getError(request);
                Optional<ExceptionRule> exceptionRuleOptional = exceptionsRules.stream()
                                .map(exceptionRule -> exceptionRule.exceptionClass().isInstance(error) ? exceptionRule
                                                : null)
                                .filter(Objects::nonNull)
                                .findFirst();
                //If the error is managed, there's no need to print the whole stack trace
                if(exceptionRuleOptional.isEmpty())
                        log.error("Intercepted Unknown Error", error);
                return exceptionRuleOptional
                                .<Map<String, Object>>map(exceptionRule -> Map.of(
                                                ErrorAttributesKey.STATUS_CODE.getKey(), exceptionRule.status().value(),
                                                ErrorAttributesKey.DESCRIPTION.getKey(), error.getMessage()))
                                .orElseGet(() -> Map.of(
                                                ErrorAttributesKey.STATUS_CODE.getKey(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                ErrorAttributesKey.DESCRIPTION.getKey(), INTERNAL_SERVER_ERROR_MESSAGE));
        }

}
