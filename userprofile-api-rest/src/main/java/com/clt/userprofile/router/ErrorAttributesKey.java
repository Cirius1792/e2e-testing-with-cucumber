package com.clt.userprofile.router;

import lombok.Getter;

/**
 * Defines the attributes of the returned Error entity in case of exceptions occurred while serving a request
 */
@Getter
enum ErrorAttributesKey {
    CODE("code"),
    STATUS_CODE("status_code"),
    DESCRIPTION("description");

    private final String key;

    ErrorAttributesKey(String key) {
        this.key = key;
    }
}