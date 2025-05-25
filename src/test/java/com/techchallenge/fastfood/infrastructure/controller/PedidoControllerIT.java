package com.techchallenge.fastfood.infrastructure.controller;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PedidoControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private PedidoCacheService pedidoCacheService;

    @Autowired
    private Environment environment;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void deveAdicionarPedido() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(1L);
        pedido.setCpf("28477593019");
        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setCriadoEm(LocalDateTime.now().withNano(0));

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pedido)
                .when()
                .post("/fastfood/pedido")
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PedidoDTO> pedidos = pedidoCacheService.listarFilaPedidos();
        assertTrue(pedidos.stream().anyMatch(p -> p.getId().equals(1L)));
    }

    @Test
    void deveListarPedidos() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(2L);
        pedido.setCpf("48654344024");
        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setCriadoEm(LocalDateTime.now().withNano(0));

        pedidoCacheService.adicionarPedidoNaFila(pedido);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/fastfood/pedido")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThan(0))
                .body("id", hasItem(2));
    }

    @Test
    void deveAtualizarStatusPedido() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(3L);
        pedido.setCpf("07823828006");
        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setCriadoEm(LocalDateTime.now().withNano(0));

        pedidoCacheService.adicionarPedidoNaFila(pedido);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/fastfood/pedido/{id}/status/{statusPedido}", 3L, StatusPedido.PRONTO)
                .then()
                .statusCode(HttpStatus.OK.value());

        PedidoDTO atualizado = pedidoCacheService.listarFilaPedidos()
                .stream()
                .filter(p -> p.getId().equals(3L))
                .findFirst()
                .orElseThrow();
        assertEquals(StatusPedido.PRONTO, atualizado.getStatusPedido());
    }

    @Test
    void deveRemoverPedido() {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setId(4L);
        pedido.setCpf("21972026003");
        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setCriadoEm(LocalDateTime.now().withNano(0));

        pedidoCacheService.adicionarPedidoNaFila(pedido);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/fastfood/pedido/{id}", 4L)
                .then()
                .statusCode(HttpStatus.OK.value());

        List<PedidoDTO> pedidos = pedidoCacheService.listarFilaPedidos();
        assertTrue(pedidos.stream().noneMatch(p -> p.getId().equals(4L)));
    }
}
