# tech-challenge-fastfood

## Visão Geral
Este repositório é dedicado ao **Tech Challenge**, um projeto interdisciplinar que integra conhecimentos de diversas disciplinas da fase de desenvolvimento. A atividade visa desenvolver um sistema de autoatendimento para uma lanchonete em expansão, com foco em melhorar a eficiência do atendimento e a satisfação dos clientes.

## Funcionalidades do Sistema

### Acompanhamento de Pedido
- **Status do Pedido**: Após confirmado e pago, o pedido é enviado à cozinha e o cliente pode acompanhar seu progresso nas etapas:
    - Recebido
    - Em preparação
    - Pronto
    - Finalizado

- **Acompanhamento de Pedidos**: Monitoramento dos pedidos em andamento e controle do tempo de espera.

## Ambiente de Desenvolvimento

- **Java 21**
- **Docker / Kubernetes**
- **Maven**: Apache Maven 3.9.9
- **Banco de Dados**: PostgreSQL 17
    - Username: `postgres`
    - Password: `1234`
---

# Diagrama de Arquitetura:

![Desenho de arquitetura.jpg](documentacao%2FDesenho%20de%20arquitetura.jpg)

# Passo a passo para execução

1. Após realizar o clone do projeto deve-se primeiro se atentar as versões do java e do maven suportadas pelo projeto de acordo com o tópico **Ambiente de Desenvolvimento** acima;
2. Posteriormente, basta rodar a seguinte sequência de scripts que seguem abaixo concatenados:
```shell
 kubectl apply -f k8s-infra/env/secret.yaml &
 kubectl apply -f k8s-infra/env/configmap.yaml &
 kubectl apply -f k8s-infra/db/postgresdb-statefulset.yaml &
 kubectl apply -f k8s-infra/db/postgresdb-service.yaml &
 kubectl apply -f k8s-infra/fastfoodapi/fastfoodapi-producao-deployment.yaml &
 kubectl apply -f k8s-infra/fastfoodapi/fastfoodapi-producao-service.yaml & 
 kubectl apply -f k8s-infra/hpa/fastfoodapi-producao-hpa.yaml
```
3. A aplicação estará disponível em http://localhost:30001/fastfood/swagger-ui/index.html
4. Segue abaixo ordem indicada para o uso das APIs:
   1. cliente-controller (cadastro e busca de clientes)
   2. produto-controller (cadastro, atualização, busca e deleção de produtos
   3. pedido-controller (cadastro, atualização e busca de pedidos)
   4. pagamento-controller (pagamento e verificação)

# Apresentação disponível no Youtube

