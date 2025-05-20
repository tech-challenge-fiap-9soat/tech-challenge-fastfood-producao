package com.techchallenge.fastfood.domain.exception;

import com.techchallenge.fastfood.infrastructure.enums.ExceptionEnum;

public class BusinessException extends RuntimeException{
    public BusinessException(ExceptionEnum message, Object... params) {
        super(String.format(message.getMessage(), params));
    }
    public BusinessException(ExceptionEnum message) {
        super(message.getMessage());
    }
}
