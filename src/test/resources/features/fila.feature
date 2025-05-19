Feature: Listagem de pedidos na fila

  Scenario: Listar todos os pedidos da fila com sucesso
    Given existem pedidos salvos na fila
    When o cliente requisita a listagem de pedidos
    Then a API retorna a lista de pedidos ordenada
