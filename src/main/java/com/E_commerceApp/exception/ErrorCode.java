package com.E_commerceApp.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error!", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error!", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed!", HttpStatus.CONFLICT),
    INVALID_USERNAME(1003, "Username must be at least 3 characters!", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User not found!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    CATEGORY_EXISTED(1007, "Category already existed!", HttpStatus.CONFLICT),
    CATEGORY_NOT_FOUND(1008, "Category not found!", HttpStatus.NOT_FOUND),
    IMAGE_UPLOAD_FAILED(1009, "Image upload failed!", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1010, "Product not found!", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(1011, "Product already existed!", HttpStatus.CONFLICT),
    CART_NOT_FOUND(1012, "Cart not found!", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1013, "Cart item not found!", HttpStatus.NOT_FOUND),
    INSUFFICIENT_STOCK(1014, "Insufficient stock!", HttpStatus.BAD_REQUEST)
    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
