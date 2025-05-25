package com.techchallenge.fastfood.gateways.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.gateways.repository.PedidoGateway;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PedidoCacheRedisGateway implements PedidoGateway {

    private final RedisPedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    public List<PedidoDTO> getObjectAndConvertCachePedidos() {
        List<PedidoDTO> objects = pedidoRepository.listarFilaPedidos();

        if (objects == null || objects.isEmpty()) return List.of();

        return objects;
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
    public PedidoDTO findById(Long id) {
        PedidoDTO objectPedido = pedidoRepository.getPedidoById(id);

        if (objectPedido == null) return null;

        return objectPedido;
    }

    @Override
    public PedidoDTO save(PedidoDTO pedido) {
        return pedidoRepository.adicionarPedidoNaFila(pedido);
    }

    @Override
    public void update(Long id, StatusPedido statusPedido) {
        pedidoRepository.atualizarStatusPedido(id, statusPedido);
    }

    @Override
    public void removeById(Long id) {
        pedidoRepository.removerPedidoCache(id);
    }

    @Override
    public void removeAll() {
        pedidoRepository.removerTodosDadosEmCache();
    }
}
