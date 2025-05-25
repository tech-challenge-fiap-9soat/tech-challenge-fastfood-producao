package com.techchallenge.fastfood.gateways.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import com.techchallenge.fastfood.infrastructure.repository.impl.RedisPedidoRepositoryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoCacheRedisGatewayTest {

    private final RedisPedidoRepository pedidoRepository = mock(RedisPedidoRepositoryMock.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PedidoCacheRedisGateway gateway = new PedidoCacheRedisGateway(pedidoRepository, objectMapper);

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Deve retornar lista de pedidos convertidos do cache")
    void deveRetornarPedidosConvertidos() {
        PedidoDTO pedido = new PedidoDTO(1L, "12345678900", StatusPedido.RECEBIDO, 10.0, LocalDateTime.now().withNano(0));
        when(pedidoRepository.listarFilaPedidos()).thenReturn(List.of(pedido));

        // ação
        List<PedidoDTO> resultado = gateway.findAllToDisplay();

        // verificação
        assertEquals(1, resultado.size());
        assertEquals(pedido.getCpf(), resultado.get(0).getCpf());
        assertEquals(StatusPedido.RECEBIDO, resultado.get(0).getStatusPedido());
    }

    @Test
    @DisplayName("Deve retornar pedido pelo ID")
    void deveRetornarPedidoPorId() {
        // cenário
        PedidoDTO pedido = new PedidoDTO(10L, "98765432100", StatusPedido.PRONTO, 10.0, LocalDateTime.now().withNano(0));
        when(pedidoRepository.getPedidoById(10L)).thenReturn(pedido);

        // ação
        PedidoDTO resultado = gateway.findById(10L);

        // verificação
        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
    }

    @Test
    @DisplayName("Deve retornar vazio quando pedido não for encontrado")
    void deveRetornarVazioSePedidoNaoEncontrado() {
        // cenário
        when(pedidoRepository.listarFilaPedidos()).thenReturn(List.of());

        // ação
        PedidoDTO resultado = gateway.findById(999L);

        // verificação
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve salvar pedido na fila e retornar o mesmo")
    void deveSalvarPedidoNaFila() {
        // cenário
        PedidoDTO pedido = new PedidoDTO(5L, "22233344455", StatusPedido.RECEBIDO, 10.0, LocalDateTime.now().withNano(0));
        when(pedidoRepository.adicionarPedidoNaFila(pedido)).thenReturn(pedido);

        // ação
        PedidoDTO resultado = gateway.save(pedido);

        // verificação
        assertEquals(pedido.getId(), resultado.getId());
        verify(pedidoRepository, times(1)).adicionarPedidoNaFila(pedido);
    }

}