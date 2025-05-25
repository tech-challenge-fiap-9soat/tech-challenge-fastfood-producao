package com.techchallenge.fastfood.usecases.pedido;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;

import java.util.List;

public interface PedidoCacheService {

    void adicionarPedidoNaFila(PedidoDTO pedido);

    List<PedidoDTO> listarFilaPedidos();

    PedidoDTO getPedidoById(Long id);

    void atualizarStatusPedido(Long id, StatusPedido statusPedido);

    void removerPedidoCache(Long id);

    void removerTodosDadosEmCache();

}
