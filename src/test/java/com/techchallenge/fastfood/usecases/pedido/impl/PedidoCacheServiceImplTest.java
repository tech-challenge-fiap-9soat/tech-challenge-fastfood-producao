package com.techchallenge.fastfood.usecases.pedido.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.gateways.repository.PedidoGateway;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PedidoCacheServiceImplTest {

    @Mock
    private PedidoGateway pedidoGateway;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PedidoCacheServiceImpl pedidoService;

    @Test
    void deveListarFilaPedidosOrdenada() {
        PedidoDTO pedido1 = new PedidoDTO(123l, "58349259351", StatusPedido.PRONTO, 10.0);
        PedidoDTO pedido2 = new PedidoDTO(456l, "58349259351", StatusPedido.RECEBIDO, 10.0);

        Mockito.when(pedidoGateway.findAllToDisplay()).thenReturn(List.of(pedido1, pedido2));

        List<PedidoDTO> resultado = pedidoService.listarFilaPedidos();

        assertEquals(2, resultado.size());
        assertEquals(StatusPedido.PRONTO, resultado.get(0).getStatusPedido());
    }

    @Test
    void deveListarOPedidoBuscado() {
        PedidoDTO pedido1 = new PedidoDTO(123l, "58349259351", StatusPedido.PRONTO, 10.0);

        Mockito.when(pedidoGateway.findById(1L)).thenReturn(pedido1);

        PedidoDTO resultado = pedidoService.getPedidoById(1L);

        assertEquals(123L, resultado.getId());
    }

    @Test
    void deveAdicionarPedidoNaFila() {
        PedidoDTO pedido = new PedidoDTO(123l, "58349259351", StatusPedido.RECEBIDO, 10.0);
        pedidoService.adicionarPedidoNaFila(pedido);
        Mockito.verify(pedidoGateway, Mockito.times(1)).save(pedido);
    }

    @Test
    void deveRemoverPrimeiroDaFila() {
        pedidoService.removerPedidoCache(10L);
        Mockito.verify(pedidoGateway).removeById(10L);
    }

}