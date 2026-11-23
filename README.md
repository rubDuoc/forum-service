# forum-service

Microservicio de **búsqueda, visualización, publicación y comentarios de temas** del Sistema de Foros en Línea
(Desarrollo Full Stack III - DSY2205). Generado con el arquetipo propio
[foro-arquetipo](https://github.com/rubDuoc/foro-arquetipo).

- Spring Boot 4 · Java 21 · Spring Data JPA · Oracle Autonomous Database (Oracle Cloud)
- Puerto local: **8084**
- 5 categorías: Tecnología, Videojuegos, Deportes, Música, Cine y Series
- Patrón de diseño: **Builder** (`ErrorResponse`, `EstadoResponse`, `ModeracionResponse`)
- Se comunica con **user-service** para conocer el rol de quien realiza cada operación

## Roles y privilegios

El header **`X-Usuario-Id`** indica qué usuario realiza la operación; el rol se consulta en user-service
(nunca se toma del cliente). El autor de un tema o comentario es siempre el del header.

| Operación | USUARIO | ADMIN |
|---|---|---|
| Ver y buscar temas, categorías y comentarios | Sí | Sí |
| Publicar temas y comentar | Sí | Sí |
| Editar o eliminar temas y comentarios | Solo los propios | Todos |
| Crear, editar o eliminar categorías | No | Sí |
| Banear o desbanear contenido (`PATCH .../moderacion`) | No | Sí |

Los temas y comentarios **baneados** dejan de aparecer en listados, búsquedas, detalle y conteos.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/estado` | Estado del servicio |
| GET | `/api/categorias` · `/api/categorias/{id}` | Categorías |
| GET | `/api/categorias/resumen` | Cantidad de temas por categoría |
| POST / PUT / DELETE | `/api/categorias[/{id}]` | Gestión de categorías (solo ADMIN) |
| GET | `/api/temas` · `/api/temas/{id}` | Temas |
| GET | `/api/temas/categoria/{id}` · `/api/temas/usuario/{id}` | Temas por categoría o autor |
| GET | `/api/temas/buscar?titulo=` | Buscar por título |
| POST / PUT / DELETE | `/api/temas[/{id}]` | Publicar, editar o eliminar temas |
| GET / POST | `/api/temas/{id}/comentarios` | Ver o agregar comentarios |
| GET | `/api/comentarios` · `/api/comentarios/{id}` · `/api/comentarios/usuario/{id}` | Comentarios |
| PUT / DELETE | `/api/comentarios/{id}` | Editar o eliminar comentarios |
| PATCH | `/api/temas/{id}/moderacion` · `/api/comentarios/{id}/moderacion` | Banear o desbanear (solo ADMIN) |

Errores en JSON: 400, 403, 404, 409, 500 y 503 (si user-service no responde).

## Base de datos

Script: `src/main/resources/db/script.sql` (tablas `CATEGORIAS`, `TEMAS` y `COMENTARIOS`, con columnas de baneo).
Orden de ejecución de los scripts del sistema: **user-service → forum-service → moderation-service**.

## Ejecutar

Requiere **user-service** corriendo. Copiar el wallet de Oracle en `wallet/` y en CMD:

```
set DB_PASSWORD=clave_de_oracle
mvnw spring-boot:run
```

Variables opcionales: `USER_SERVICE_URL` (por defecto `http://localhost:8083`), `DB_USERNAME`, `DB_SERVICE`,
`TNS_ADMIN`, `PORT`, `CORS_ORIGENES`.

## Despliegue en la nube (Render)

URL publica: **https://foro-forum-service-1-0.onrender.com** (por ejemplo, `/api/estado`).
Imagen Docker publicada en Docker Hub y desplegada en Render; el wallet de Oracle llega como archivo secreto.

## Postman

Colección con tests automáticos en `postman/forum-service.postman_collection.json`
(importar en Postman junto con el entorno `postman/render.postman_environment.json` para probar contra Render → *Run collection*). Crea y elimina sus propios datos de prueba.
