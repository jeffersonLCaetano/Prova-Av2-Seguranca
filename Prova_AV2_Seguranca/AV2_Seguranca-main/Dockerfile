FROM ubuntu:latest AS build

# Atualiza o repositório e instala o OpenJDK
RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

# Copia o código da aplicação para dentro da imagem Docker
COPY . .

# Instala o Maven
RUN apt-get install maven -y

# Compila o projeto e pula os testes
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim

# Expõe a porta onde a aplicação vai rodar
EXPOSE 8080

# Copia o arquivo JAR gerado no estágio anterior para o novo contêiner
COPY --from=build /target/prova-0.0.1-SNAPSHOT.jar app.jar

# Define o comando de execução do contêiner
ENTRYPOINT [ "java", "-jar", "app.jar" ]