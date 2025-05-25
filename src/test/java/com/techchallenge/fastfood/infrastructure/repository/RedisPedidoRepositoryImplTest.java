package com.techchallenge.fastfood.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.impl.RedisPedidoRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisPedidoRepositoryImplTest {
    private final RedisTemplate<String, PedidoDTO> redisTemplate = mock(RedisTemplate.class);
    private final ValueOperations<String, PedidoDTO> valueOperations = mock(ValueOperations.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisPedidoRepository repository = new RedisPedidoRepositoryImpl(redisTemplate, objectMapper);

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Deve adicionar pedido na fila com sucesso")
    void deveAdicionarPedidoNaFila() {
        PedidoDTO pedido = new PedidoDTO(1L, "12345678900", StatusPedido.RECEBIDO, 10.0);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        PedidoDTO resultado = repository.adicionarPedidoNaFila(pedido);

        verify(valueOperations).set("pedido:1", pedido);
        assertEquals(pedido, resultado);
    }

    @Test
    @DisplayName("Deve listar todos os pedidos salvos")
    void deveListarTodosPedidos() {
        Set<String> chaves = Set.of("pedido:1", "pedido:2");
        PedidoDTO p1 = new PedidoDTO(1L, "111", StatusPedido.RECEBIDO, 10.0);
        PedidoDTO p2 = new PedidoDTO(2L, "222", StatusPedido.PRONTO, 10.0);

        when(redisTemplate.keys("pedido:*")).thenReturn(chaves);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("pedido:1")).thenReturn(p1);
        when(valueOperations.get("pedido:2")).thenReturn(p2);

        List<PedidoDTO> pedidos = repository.listarFilaPedidos();

        assertEquals(2, pedidos.size());
        assertTrue(pedidos.contains(p1));
        assertTrue(pedidos.contains(p2));
    }

    @Test
    @DisplayName("Deve listar o pedido buscado caso exista")
    void deveListarOPedido() {
        Set<String> chaves = Set.of("pedido:1");
        PedidoDTO p1 = new PedidoDTO(1L, "111", StatusPedido.RECEBIDO, 10.0);

        when(redisTemplate.keys("pedido:1")).thenReturn(chaves);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("pedido:1")).thenReturn(p1);

        Object objectPedido = repository.getPedidoById(1L);

        PedidoDTO pedido = objectMapper.convertValue(objectPedido, PedidoDTO.class);

        assertTrue(pedido.getId().equals(1L));
    }

    @Test
    @DisplayName("Deve atualizar status do pedido para PRONTO com TTL")
    void deveAtualizarStatusParaProntoComTTL() {
        PedidoDTO pedido = new PedidoDTO(1L, "123", StatusPedido.RECEBIDO, 10.0);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("pedido:1")).thenReturn(pedido);

        repository.atualizarStatusPedido(1L, StatusPedido.PRONTO);

        ArgumentCaptor<PedidoDTO> captor = ArgumentCaptor.forClass(PedidoDTO.class);
        verify(valueOperations).set(eq("pedido:1"), captor.capture(), eq(Duration.ofMinutes(1)));
        assertEquals(StatusPedido.PRONTO, captor.getValue().getStatusPedido());
    }

    @Test
    @DisplayName("Deve atualizar status do pedido normalmente se não for PRONTO ou CANCELADO")
    void deveAtualizarStatusNormalmente() {
        PedidoDTO pedido = new PedidoDTO(1L, "123", StatusPedido.RECEBIDO, 10.0);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("pedido:1")).thenReturn(pedido);

        repository.atualizarStatusPedido(1L, StatusPedido.RECEBIDO);

        verify(valueOperations).set(eq("pedido:1"), any(PedidoDTO.class));
    }

    @Test
    @DisplayName("Deve remover pedido se status for CANCELADO")
    void deveRemoverPedidoSeCancelado() {
        PedidoDTO pedido = new PedidoDTO(1L, "123", StatusPedido.RECEBIDO, 10.0);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("pedido:1")).thenReturn(pedido);

        repository.atualizarStatusPedido(1L, StatusPedido.CANCELADO);

        verify(redisTemplate).delete("pedido:1");
    }

    @Test
    @DisplayName("Deve remover pedido por ID")
    void deveRemoverPedido() {
        repository.removerPedidoCache(99L);

        verify(redisTemplate).delete("pedido:99");
    }

}