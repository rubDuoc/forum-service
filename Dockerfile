# Etapa 1: compila el microservicio con Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# Etapa 2: imagen liviana solo con el JRE, ejecutada con un usuario sin privilegios
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --no-create-home app
COPY --from=build /app/target/*.jar app.jar
USER app
# El wallet de Oracle NO va en la imagen: se monta en esta carpeta (local) o se usa /etc/secrets (Render)
ENV TNS_ADMIN=/app/wallet
EXPOSE 8084
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
