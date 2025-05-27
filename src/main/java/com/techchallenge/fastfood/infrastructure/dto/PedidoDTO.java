package com.techchallenge.fastfood.infrastructure.dto;

import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private Long id;
    private String cpf;
    private StatusPedido statusPedido;
    private Double valorTotal;
}
