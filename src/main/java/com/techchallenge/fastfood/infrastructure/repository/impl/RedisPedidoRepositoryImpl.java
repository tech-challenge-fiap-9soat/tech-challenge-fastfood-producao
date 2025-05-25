package com.techchallenge.fastfood.infrastructure.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.infrastructure.repository.RedisPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Primary
@Profile("!test")
public class RedisPedidoRepositoryImpl implements RedisPedidoRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PEDIDO_KEY = "pedido:";

    private final ObjectMapper objectMapper;

    @Override
    public PedidoDTO adicionarPedidoNaFila(PedidoDTO pedido) {
        redisTemplate.opsForValue().set(PEDIDO_KEY+pedido.getId(), pedido);
        return pedido;
    }

    @Override
    public List<Object> listarFilaPedidos() {
        Set<String> chaves = redisTemplate.keys(PEDIDO_KEY+"*");

        if (chaves == null || chaves.isEmpty()) {
            return List.of();
        }

        return chaves.stream()
                .map(redisTemplate.opsForValue()::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Object getPedidoById(Long id) {
        Set<String> chaves = redisTemplate.keys(PEDIDO_KEY+id);

        if (chaves == null || chaves.isEmpty()) {
            return null;
        }

        return chaves.stream()
                .map(redisTemplate.opsForValue()::get)
                .filter(Objects::nonNull)
                .findFirst().get();
    }

    @Override
    public void atualizarStatusPedido(Long id, StatusPedido statusPedido) {
        String chave = PEDIDO_KEY + id;

        Object obj = redisTemplate.opsForValue().get(chave);

        if (obj == null) return;

        PedidoDTO pedido = objectMapper.convertValue(obj, PedidoDTO.class);
        pedido.setStatusPedido(statusPedido);

        if (StatusPedido.PRONTO.equals(statusPedido)) {
            redisTemplate.opsForValue().set(chave, pedido, Duration.ofMinutes(1));
        } else if (StatusPedido.CANCELADO.equals(statusPedido)) {
            removerPedidoCache(id);
        } else {
            redisTemplate.opsForValue().set(chave, pedido);
        }
    }

    @Override
    public void removerPedidoCache(Long id) {
        redisTemplate.delete(PEDIDO_KEY + id);
    }

    @Override
    public void removerTodosDadosEmCache() {
        redisTemplate.delete(PEDIDO_KEY+"*");
    }

}
