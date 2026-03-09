# Eclipse Hotel: Desafio de Desenvolvimento com Spring Boot

Esta é uma API RESTful para gerenciamento de um hotel, desenvolvida como solução para um desafio de desenvolvimento back-end. O sistema permite gerenciar clientes, quartos e reservas, seguindo as melhores práticas de desenvolvimento com Spring Boot.

Este projeto está totalmente containerizado usando Docker e Docker Compose, garantindo um ambiente de desenvolvimento consistente, persistente e de fácil configuração.

---

## ✨ Features

*   **Gerenciamento de Clientes (CRUD):** Operações completas para criar, ler, atualizar e deletar clientes.
*   **Gerenciamento de Quartos (CRUD):** Operações completas para criar, ler, atualizar e deletar quartos.
*   **Sistema de Reservas:**
    *   Criação de novas reservas com validação de disponibilidade.
    *   Cancelamento e finalização de reservas.
    *   Consulta de reservas por intervalo de datas.
    *   Visualização dos quartos que estão ocupados no momento.
*   **Integração com ViaCEP:** Busca automática de endereço a partir do CEP durante a criação ou atualização de um cliente.
*   **Cache:** Implementação de cache com TTL de 30 segundos nas principais consultas para otimizar a performance.
*   **Logs:** Logs detalhados em todas as operações para facilitar o troubleshooting.
*   **Testes Unitários:** Cobertura de testes para a camada de serviço, garantindo a lógica de negócio.
*   **Documentação da API:** Documentação interativa disponível via Swagger (OpenAPI).

---

## 🚀 Tecnologias Utilizadas

*   **Backend:** Java 21, Spring Boot 3, Spring Data JPA
*   **Banco de Dados:** PostgreSQL 13 (para Docker) e H2 (para execução local)
*   **Documentação da API:** SpringDoc OpenAPI (Swagger)
*   **Ambiente:** Docker & Docker Compose
*   **Build e Dependências:** Maven
*   **Testes:** JUnit 5 & Mockito
*   **Utilitários:** Lombok

---

## Endpoints da API

Abaixo estão os principais endpoints disponíveis na aplicação. Para mais detalhes e para interagir com a API, acesse a [Documentação do Swagger](http://localhost:8080/swagger-ui.html).

| Verbo HTTP | Endpoint | Descrição |
| :--- | :--- | :--- |
| **Clientes** |
| `GET` | `/customers` | Lista todos os clientes. |
| `GET` | `/customers/{id}` | Busca um cliente específico pelo ID. |
| `POST` | `/customers` | Cria um novo cliente. |
| `PUT` | `/customers/{id}` | Atualiza os dados de um cliente existente. |
| `DELETE` | `/customers/{id}` | Remove um cliente (se não tiver reservas). |
| **Quartos** |
| `GET` | `/rooms` | Lista todos os quartos. |
| `GET` | `/rooms/available`| Lista quartos disponíveis para um período (requer `checkin` e `checkout` como parâmetros). |
| `GET` | `/rooms/occupied`| Lista quartos ocupados no momento. |
| `GET` | `/rooms/{id}` | Busca um quarto específico pelo ID. |
| `POST` | `/rooms` | Adiciona um novo quarto ao hotel. |
| `PUT` | `/rooms/{id}` | Atualiza os dados de um quarto existente. |
| `DELETE` | `/rooms/{id}` | Remove um quarto (se não tiver reservas). |
| **Reservas** |
| `GET` | `/reservations` | Lista todas as reservas (pode ser filtrada por datas `checkin` e `checkout`). |
| `GET` | `/reservations/{id}` | Busca uma reserva específica pelo ID. |
| `POST` | `/reservations` | Cria uma nova reserva para um cliente em um quarto. |
| `PATCH` | `/reservations/{id}/cancel` | Cancela uma reserva com status `SCHEDULED`. |

---

## ⚙️ Como Executar o Projeto

Você pode executar o projeto de duas maneiras. O método com Docker é o mais recomendado.

### Método 1: Com Docker (Recomendado)

Esta abordagem irá configurar e executar a aplicação e o banco de dados PostgreSQL em contêineres isolados.

**Pré-requisitos:**
*   [Docker](https://www.docker.com/get-started)
*   [Docker Compose](https://docs.docker.com/compose/install/)

**Passos:**

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/gaelcoder/EclipseHotel
    ```

2.  **Navegue até a pasta do projeto:**
    ```bash
    cd EclipseHotel
    ```

3.  **Construa e inicie os contêineres:**
    ```bash
    docker-compose up --build
    ```
    Este comando irá construir a imagem da aplicação, baixar a imagem do PostgreSQL e iniciar ambos os serviços. Os dados do banco serão persistidos em um volume Docker.

4.  **Acesse a aplicação:**
    *   **API Base:** [http://localhost:8080/](http://localhost:8080/)
    *   **Documentação da API (Swagger):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

### Método 2: Localmente com Maven

Esta abordagem executará a aplicação diretamente na sua máquina, utilizando um banco de dados em memória (H2), o que significa que os dados serão perdidos a cada reinicialização.

**Pré-requisitos:**
*   JDK 21 ou superior.
*   Apache Maven 3.6 ou superior.

**Passos:**

1.  **Clone o repositório e entre na pasta:**
    ```bash
    git clone https://github.com/gaelcoder/EclipseHotel
    cd EclipseHotel
    ```

2.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

3.  **Acesse a aplicação:**
    *   **API:** [http://localhost:8080/](http://localhost:8080/)
    *   **Console do Banco H2:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (Use o JDBC URL `jdbc:h2:mem:testdb`)

---

## 🧪 Como Executar os Testes

Para rodar a suíte de testes unitários e garantir que toda a lógica de negócio está funcionando como esperado, utilize o comando:

```bash
mvn clean test
```

---

## 👨‍💻 Autor

Projeto desenvolvido por **Gabriel Lima de Souza Azevedo**.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/gabrielsaz/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/gaelcoder)
