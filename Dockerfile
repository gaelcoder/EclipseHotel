FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

# Baixa todas as dependências do projeto.
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package -DskipTests

# Imagem mais leve, apenas com o Java Runtime Environment (JRE)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta em que a aplicação Spring Boot roda
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for executado
ENTRYPOINT ["java", "-jar", "app.jar"]
