# Usar a imagem oficial do OpenJDK 17 baseada no Alpine
FROM openjdk:17-alpine

# Definir o diretório de trabalho no contêiner
WORKDIR /app

# Copiar o arquivo JAR para o contêiner
COPY target/api-0.0.1-SNAPSHOT.jar /app/seu-app.jar

# Expor a porta 8080
EXPOSE 8080

# Comando para rodar o aplicativo Java
ENTRYPOINT ["java", "-jar", "seu-app.jar"]