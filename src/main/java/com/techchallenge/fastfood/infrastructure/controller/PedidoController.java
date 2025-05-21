package com.techchallenge.fastfood.infrastructure.controller;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import com.techchallenge.fastfood.infrastructure.enums.StatusPedido;
import com.techchallenge.fastfood.usecases.pedido.PedidoCacheService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoCacheService pedidoCacheService;

    @PostMapping
    public void adicionar(@RequestBody @Valid PedidoDTO pedido) {
        pedidoCacheService.adicionarPedidoNaFila(pedido);
    }

    @GetMapping
    public List<PedidoDTO> listar() {
        return pedidoCacheService.listarFilaPedidos();
    }

    @PatchMapping("/{id}/status/{statusPedido}")
    public ResponseEntity<Void> atualizarStatus(@PathVariable Long id, @PathVariable StatusPedido statusPedido) {
        pedidoCacheService.atualizarStatusPedido(id, statusPedido);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPedido(@PathVariable Long id) {
        pedidoCacheService.removerPedidoCache(id);
        return ResponseEntity.ok().build();
    }

}
