package com.techchallenge.fastfood.infrastructure.repository;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;

import java.util.List;

public interface RedisPedidoRepository {

    PedidoDTO adicionarPedidoNaFila(PedidoDTO pedido);

    List<PedidoDTO> listarFilaPedidos();

    PedidoDTO getPedidoById(Long id);

    void atualizarStatusPedido(Long id, StatusPedido statusPedido);

    void removerPedidoCache(Long id);

    void removerTodosDadosEmCache();
}
