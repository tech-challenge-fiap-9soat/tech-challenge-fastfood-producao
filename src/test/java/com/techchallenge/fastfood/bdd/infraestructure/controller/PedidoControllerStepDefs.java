package com.techchallenge.fastfood.bdd.infraestructure.controller;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PedidoControllerStepDefs {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PedidoCacheService pedidoService;

    private ResultActions resultado;

    @BeforeEach
    public void limparFilaAntesDoCenario() {
        pedidoService.removerTodosDadosEmCache();
    }

    @Given("existem pedidos salvos na fila")
    public void existem_pedidos_salvos_na_fila() {
        PedidoDTO pedido1 = new PedidoDTO(123l, "58349259351", StatusPedido.RECEBIDO, 10.0);
        PedidoDTO pedido2 = new PedidoDTO(456l, "58349259351", StatusPedido.PRONTO, 10.0);
        pedidoService.adicionarPedidoNaFila(pedido1);
        pedidoService.adicionarPedidoNaFila(pedido2);
    }

    @When("o cliente requisita a listagem de pedidos")
    public void o_cliente_requisita_a_listagem_de_pedidos() throws Exception {
        resultado = mockMvc.perform(MockMvcRequestBuilders.get("/pedido")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("a API retorna a lista de pedidos ordenada")
    public void a_api_retorna_a_lista_de_pedidos_ordenada() throws Exception {
        resultado.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].statusPedido").value("PRONTO"));
    }
}
