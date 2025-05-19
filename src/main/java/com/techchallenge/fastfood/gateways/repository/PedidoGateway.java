package com.techchallenge.fastfood.gateways.repository;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;

import java.util.List;
import java.util.Optional;

public interface PedidoGateway {

    List<PedidoDTO> findAllToDisplay();

    Optional<PedidoDTO> findById(Long id);

    PedidoDTO save(PedidoDTO pedidoEntity);

}
