package com.techchallenge.fastfood.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long id;
    private String cpf;
    private StatusPedido statusPedido;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime criadoEm;
}
