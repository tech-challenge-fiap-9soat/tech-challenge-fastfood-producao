package com.techchallenge.fastfood.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JacksonConfigTest {

    @Test
    @DisplayName("Deve serializar e desserializar corretamente com o formato yyyy-MM-dd HH:mm:ss")
    void deveSerializarEDesserializarComFormatoEsperado() throws Exception {
        // cenário
        JacksonConfig config = new JacksonConfig();
        ObjectMapper mapper = config.objectMapper();

        PedidoDTO pedido = new PedidoDTO(
                1L,
                "12345678900",
                StatusPedido.RECEBIDO,
                LocalDateTime.of(2025, 5, 18, 20, 0, 0)
        );

        // ação - serializa
        String json = mapper.writeValueAsString(pedido);

        // verificação da serialização
        assertTrue(json.contains("\"criadoEm\":\"2025-05-18 20:00:00\""));

        // ação - desserializa
        PedidoDTO resultado = mapper.readValue(json, PedidoDTO.class);

        // verificação da desserialização
        assertEquals(pedido.getCriadoEm(), resultado.getCriadoEm());
        assertEquals(pedido.getCpf(), resultado.getCpf());
        assertEquals(pedido.getStatusPedido(), resultado.getStatusPedido());
    }

}