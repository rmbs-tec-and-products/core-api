# core-api

Backend REST com Spring Boot 4.1.1, Java 21, Spring Security, JPA, MySQL/H2 e OpenAPI/Swagger.

## Rodar localmente

```bash
mvn spring-boot:run
```

A API sobe em: http://localhost:8081

### Health público

```text
GET http://localhost:8081/api/v1/health
```

### Swagger

```text
http://localhost:8081/swagger-ui.html
```

### Endpoint ADMIN

```text
GET http://localhost:8081/api/v1/admin/check
```

Autenticação HTTP Basic local:

- USER: `user` / `user123`
- ADMIN: `admin` / `admin123`

## Banco

Por padrão usa perfil `local` com H2 em memória para permitir subir o projeto imediatamente.

Para AWS/MySQL:

```bash
SPRING_PROFILES_ACTIVE=aws
DB_URL=jdbc:mysql://HOST:3306/BANCO
DB_USERNAME=usuario
DB_PASSWORD=senha
```

Na AWS esses valores serão externalizados, preferencialmente via Secrets Manager/ECS.
