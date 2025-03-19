package com.E_commerceApp.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!"),
    INVALID_KEY(1001, "Uncategorized error!"),
    USER_EXISTED(1002, "User existed!"),
    INVALID_USERNAME(1003, "Username must be at least 3 characters!"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters!"),
    USER_NOT_FOUND(1005, "User not found!"),
    UNAUTHENTICATED(1006, "Unauthenticated!"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
