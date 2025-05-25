package com.techchallenge.fastfood.usecases.pedido.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.gateways.repository.PedidoGateway;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
@Slf4j
public class PedidoCacheServiceImpl implements PedidoCacheService {

    private final PedidoGateway pedidoCacheGateway;

    @Override
    public List<PedidoDTO> listarFilaPedidos() {
        return pedidoCacheGateway.findAllToDisplay();
    }

    @Override
    public PedidoDTO getPedidoById(Long id) {
        return pedidoCacheGateway.findById(id);
    }

    @Override
    public void adicionarPedidoNaFila(PedidoDTO pedido) {
        pedidoCacheGateway.save(pedido);
    }

    @Override
    public void atualizarStatusPedido(Long id, StatusPedido statusPedido) {
        pedidoCacheGateway.update(id, statusPedido);
    }

    @Override
    public void removerPedidoCache(Long id) {
        pedidoCacheGateway.removeById(id);
    }

    @Override
    public void removerTodosDadosEmCache() {
        pedidoCacheGateway.removeAll();
    }

}
