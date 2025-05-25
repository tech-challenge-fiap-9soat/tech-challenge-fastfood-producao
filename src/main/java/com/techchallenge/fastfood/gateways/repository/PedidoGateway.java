package com.techchallenge.fastfood.gateways.repository;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoGateway {

    List<PedidoDTO> findAllToDisplay();

    PedidoDTO findById(Long id);

    PedidoDTO save(PedidoDTO pedido);

    void update(Long id, StatusPedido statusPedido);

    void removeById(Long id);

    void removeAll();

}
