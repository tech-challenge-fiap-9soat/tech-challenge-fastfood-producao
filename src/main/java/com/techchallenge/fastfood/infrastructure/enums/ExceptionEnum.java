package com.techchallenge.fastfood.infrastructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    PAGAMENTO_PENDENTE("O pagamento do pedido %s ainda está pendente.");

    private String message;
}
