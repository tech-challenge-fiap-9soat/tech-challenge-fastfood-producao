# Tech Challenge FastFood Produção

Este projeto faz parte do Tech Challenge da pós-graduação em Arquitetura de Software da FIAP. Ele representa o microsserviço responsável pela gestão do processo de produção dos pedidos no sistema de autoatendimento de uma lanchonete em expansão.

## 📚 Visão Geral

O **FastFood Produção** é um microsserviço desenvolvido em Java, utilizando o framework Spring Boot, que gerencia a fila de produção dos pedidos na cozinha. Ele é responsável por controlar e atualizar os status de cada etapa do preparo, proporcionando visibilidade em tempo real para a equipe operacional e garantindo uma produção eficiente e organizada dos pedidos.

## 🚀 Tecnologias Utilizadas

- **Linguagem:** Java 21  
- **Framework:** Spring Boot  
- **Gerenciador de Dependências:** Maven 3.9.9  
- **Banco de Dados:** PostgreSQL 17  
- **Containerização:** Docker  
- **Orquestração:** Kubernetes  

## 🛠️ Configuração e Execução

1. **Pré-requisitos:**
   - Java 21
   - Maven 3.9.9
   - Docker
   - Kubernetes com `kubectl` configurado

2. **Clone o repositório:**

   ```bash
   git clone https://github.com/tech-challenge-fiap-9soat/tech-challenge-fastfood-producao.git
   cd tech-challenge-fastfood-producao
   ```
## Implante os recursos no Kubernetes:

3. Execute os seguintes comandos para aplicar os manifests:

```bash
    kubectl apply -f k8s-infra/db/redis-deployment.yaml
    kubectl apply -f k8s-infra/db/redis-service.yaml
    kubectl apply -f k8s-infra/fastfoodapi/fastfoodapi-producao-deployment.yaml
    kubectl apply -f k8s-infra/fastfoodapi/fastfoodapi-producao-service.yaml
    kubectl apply -f k8s-infra/hpa/fastfoodapi-producao-hpa.yaml
```

Acesse a aplicação:

Após a implantação, a aplicação estará disponível em: http://localhost:30002/fastfood/swagger-ui/index.html

##📄 Documentação
A documentação completa da API, incluindo os endpoints disponíveis, pode ser acessada via Swagger UI no link fornecido acima.

## Segue Evidência de testes e coberturas:

![image](https://github.com/user-attachments/assets/2fcf5ccb-f65e-4cea-b1cd-56ebc4663331)

#### segue os casos de testes, o ultimo "Gerenciar pagamentos de pedidos é o BDD com Cucumber

![image](https://github.com/user-attachments/assets/6dfc1454-1e17-424b-8c0a-d7f5203ece1b)


