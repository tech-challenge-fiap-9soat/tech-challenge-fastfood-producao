package com.techchallenge.fastfood.infrastructure.repository.impl;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
@Profile("test")
public class RedisPedidoRepositoryMock implements RedisPedidoRepository {
    private final Map<Long, PedidoDTO> cache = new ConcurrentHashMap<>();

    @Override
    public PedidoDTO adicionarPedidoNaFila(PedidoDTO pedido) {
        cache.put(pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public List<Object> listarFilaPedidos() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public void atualizarStatusPedido(Long id, StatusPedido statusPedido) {
        PedidoDTO pedido = cache.get(id);
        if (pedido != null) {
            pedido.setStatusPedido(statusPedido);
        }
    }

    @Override
    public void removerPedidoCache(Long id) {
        cache.remove(id);
    }

    @Override
    public void removerTodosDadosEmCache() {
        cache.clear();
    }
}
