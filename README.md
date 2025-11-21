# Eclipse Hotel: Desafio para estágio em Desenvolvimento Bakc-End em Java

Esta é uma API RESTful para gerenciamento de um hotel, desenvolvida como solução para um desafio de desenvolvimento back-end. O sistema permite gerenciar clientes, quartos e reservas, seguindo as melhores práticas de desenvolvimento com Spring Boot.

## Funcionalidades Implementadas

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

## Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 3**
*   **Spring Data JPA / Hibernate**
*   **Spring Web**
*   **Banco de Dados em Memória (H2)**: Facilita a execução e testes sem necessidade de configuração externa.
*   **Lombok**: Para reduzir o código boilerplate.
*   **Maven**: Para gerenciamento de dependências e build do projeto.
*   **JUnit 5 & Mockito**: Para os testes unitários.
*   **SpringDoc OpenAPI (Swagger)**: Para a documentação da API.

## Como Executar a Aplicação

**Pré-requisitos:**
*   JDK 21 ou superior.
*   Apache Maven 3.6 ou superior.

1.  **Clone o repositório:**
    ```bash
    git clone  https://bitbucket.org/gaelcoder/javaestagiariogabriellima.git
    cd ohoteleclipse
    ```

2.  **Execute a aplicação usando o Maven:**
    ```bash
    mvn spring-boot:run
    ```
    A API estará disponível em `http://localhost:8080`.

## Como Executar os Testes

Para rodar a suíte de testes unitários e garantir que toda a lógica de negócio está funcionando como esperado, utilize o comando:

```bash
mvn clean test
```