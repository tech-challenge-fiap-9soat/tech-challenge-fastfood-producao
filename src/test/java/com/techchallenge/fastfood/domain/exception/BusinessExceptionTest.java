package com.techchallenge.fastfood.domain.exception;

import com.techchallenge.fastfood.infrastructure.enums.ExceptionEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessExceptionTest {

    @Test
    @DisplayName("Deve criar BusinessException com mensagem simples do enum")
    void deveCriarBusinessExceptionComMensagemSimples() {
        ExceptionEnum exceptionEnum = mock(ExceptionEnum.class);
        when(exceptionEnum.getMessage()).thenReturn("Erro de negócio genérico");

        BusinessException exception = new BusinessException(exceptionEnum);

        assertEquals("Erro de negócio genérico", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar BusinessException formatando mensagem com parâmetros")
    void deveCriarBusinessExceptionComParametros() {
        ExceptionEnum exceptionEnum = mock(ExceptionEnum.class);
        when(exceptionEnum.getMessage()).thenReturn("Campo %s é obrigatório");

        BusinessException exception = new BusinessException(exceptionEnum, "email");

        assertEquals("Campo email é obrigatório", exception.getMessage());
    }

}