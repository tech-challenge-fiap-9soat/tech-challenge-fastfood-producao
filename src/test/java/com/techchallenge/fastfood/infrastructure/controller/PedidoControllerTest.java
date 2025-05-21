package com.techchallenge.fastfood.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoCacheService pedidoCacheService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Deve adicionar um pedido na fila com sucesso")
    void deveAdicionarPedidoNaFila() throws Exception {
        PedidoDTO pedido = new PedidoDTO(1L, "50328074861", StatusPedido.RECEBIDO, 10.0, LocalDateTime.now().withNano(0));

        mockMvc.perform(post("/pedido")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());

        verify(pedidoCacheService).adicionarPedidoNaFila(eq(pedido));
    }

    @Test
    @DisplayName("Deve listar todos os pedidos")
    void deveListarPedidos() throws Exception {
        PedidoDTO pedido = new PedidoDTO(1L, "50328074861", StatusPedido.RECEBIDO, 10.0, LocalDateTime.now().withNano(0));
        when(pedidoCacheService.listarFilaPedidos()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/pedido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cpf").value("50328074861"));
    }

    @Test
    @DisplayName("Deve atualizar o status do pedido")
    void deveAtualizarStatusPedido() throws Exception {
        mockMvc.perform(patch("/pedido/1/status/PRONTO"))
                .andExpect(status().isOk());

        verify(pedidoCacheService).atualizarStatusPedido(1L, StatusPedido.PRONTO);
    }

    @Test
    @DisplayName("Deve remover o pedido pelo id")
    void deveRemoverPedido() throws Exception {
        mockMvc.perform(delete("/pedido/1"))
                .andExpect(status().isOk());

        verify(pedidoCacheService).removerPedidoCache(1L);
    }
}