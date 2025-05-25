package com.techchallenge.fastfood.infrastructure.controller;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoCacheService pedidoCacheService;

    @PostMapping
    public void adicionar(@RequestBody @Valid PedidoDTO pedido) {
        log.info("[POST] adicionando pedido - init...");
        pedidoCacheService.adicionarPedidoNaFila(pedido);
        log.info("[POST] adicionando pedido - finished...");
    }

    @GetMapping
    public List<PedidoDTO> listar() {
        log.info("[GET] listar pedido - init...");
        List<PedidoDTO> pedidos = pedidoCacheService.listarFilaPedidos();
        log.info("[GET] listar pedido - finished...");
        return pedidos;
    }

    @GetMapping("/{id}")
    public PedidoDTO getPedido(@PathVariable Long id) {
        log.info("[GET] get pedido pelo id - init...");
        PedidoDTO pedido = pedidoCacheService.getPedidoById(id);
        log.info("[GET] get pedido pelo id - finished...");
        return pedido;
    }

    @PatchMapping("/{id}/status/{statusPedido}")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @PathVariable StatusPedido statusPedido) {
        log.info("[PATCH] atualizarStatus pedido - init...");
        pedidoCacheService.atualizarStatusPedido(id, statusPedido);
        log.info("[PATCH] atualizarStatus pedido - finished...");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPedido(@PathVariable Long id) {
        log.info("[DELETE] remover pedido - init...");
        pedidoCacheService.removerPedidoCache(id);
        log.info("[DELETE] remover pedido - finished...");
        return ResponseEntity.ok().build();
    }

}
