package com.techchallenge.fastfood.gateways.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.gateways.repository.PedidoGateway;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PedidoCacheRedisGateway implements PedidoGateway {

    private final RedisPedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    public List<PedidoDTO> getObjectAndConvertCachePedidos() {
        List<Object> objects = pedidoRepository.listarFilaPedidos();

        if (objects == null || objects.isEmpty()) return List.of();

        return objects.stream().map(object -> objectMapper.convertValue(object, PedidoDTO.class)).toList();
    }

    @Override
    public List<PedidoDTO> findAllToDisplay() {
        List<PedidoDTO> pedidos = getObjectAndConvertCachePedidos();

        return pedidos.stream()
                .sorted(Comparator.comparing(
                        (PedidoDTO p) -> p.getStatusPedido().getId(),
                        Comparator.reverseOrder()
                ).thenComparing(
                        PedidoDTO::getCriadoEm,
                        Comparator.reverseOrder()
                ))
                .toList();
    }

    @Override
    public Optional<PedidoDTO> findById(Long id) {
        List<PedidoDTO> pedidos = getObjectAndConvertCachePedidos();
        return pedidos.stream().filter(pedido -> pedido.getId().equals(id)).findFirst();
    }

    @Override
    public PedidoDTO save(PedidoDTO pedido) {
        return pedidoRepository.adicionarPedidoNaFila(pedido);
    }
}
