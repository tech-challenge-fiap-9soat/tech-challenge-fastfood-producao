package com.techchallenge.fastfood.usecases.pedido.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class PedidoCacheServiceImpl implements PedidoCacheService {

    private final RedisPedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    public List<PedidoDTO> getObjectAndConvertCachePedidos() {
        List<Object> objects = pedidoRepository.listarFilaPedidos();

        if (objects == null || objects.isEmpty()) return List.of();

        return objects.stream().map(object -> objectMapper.convertValue(object, PedidoDTO.class)).toList();
    }

    @Override
    public List<PedidoDTO> listarFilaPedidos() {
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
    public void adicionarPedidoNaFila(PedidoDTO pedido) {
        pedidoRepository.adicionarPedidoNaFila(pedido);
    }

    @Override
    public void atualizarStatusPedido(Long id, StatusPedido statusPedido) {
        pedidoRepository.atualizarStatusPedido(id, statusPedido);
    }

    @Override
    public void removerPedidoCache(Long id) {
        pedidoRepository.removerPedidoCache(id);
    }

    @Override
    public void removerTodosDadosEmCache() {
        pedidoRepository.removerTodosDadosEmCache();
    }

}
