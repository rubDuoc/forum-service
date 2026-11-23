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
COPY --chmod=755 docker-entrypoint.sh /app/docker-entrypoint.sh
USER app
# El wallet de Oracle NO va en la imagen: se monta en esta carpeta (local)
# o llega como archivo secreto en Render (ver docker-entrypoint.sh)
ENV TNS_ADMIN=/app/wallet
EXPOSE 8084
ENTRYPOINT ["/app/docker-entrypoint.sh"]
