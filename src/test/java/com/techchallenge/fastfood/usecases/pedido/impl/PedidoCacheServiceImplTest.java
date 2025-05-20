package com.techchallenge.fastfood.usecases.pedido.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PedidoCacheServiceImplTest {

    @Mock
    private RedisPedidoRepository pedidoRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PedidoCacheServiceImpl pedidoService;

    @Test
    void deveListarFilaPedidosOrdenada() {
        PedidoDTO pedido1 = new PedidoDTO(123l, "58349259351", StatusPedido.RECEBIDO, LocalDateTime.now().minusMinutes(2));
        PedidoDTO pedido2 = new PedidoDTO(456l, "58349259351", StatusPedido.PRONTO, LocalDateTime.now());

        Object obj1 = new Object();
        Object obj2 = new Object();

        Mockito.when(pedidoRepository.listarFilaPedidos()).thenReturn(List.of(obj1, obj2));
        Mockito.when(objectMapper.convertValue(obj1, PedidoDTO.class)).thenReturn(pedido1);
        Mockito.when(objectMapper.convertValue(obj2, PedidoDTO.class)).thenReturn(pedido2);

        List<PedidoDTO> resultado = pedidoService.listarFilaPedidos();

        assertEquals(2, resultado.size());
        assertEquals(StatusPedido.PRONTO, resultado.get(0).getStatusPedido());
    }

    @Test
    void deveAdicionarPedidoNaFila() {
        PedidoDTO pedido = new PedidoDTO(123l, "58349259351", StatusPedido.RECEBIDO, LocalDateTime.now().minusMinutes(2));
        pedidoService.adicionarPedidoNaFila(pedido);
        Mockito.verify(pedidoRepository, Mockito.times(1)).adicionarPedidoNaFila(pedido);
    }

    @Test
    void deveRemoverPrimeiroDaFila() {
        pedidoService.removerPedidoCache(10L);
        Mockito.verify(pedidoRepository).removerPedidoCache(10L);
    }

}