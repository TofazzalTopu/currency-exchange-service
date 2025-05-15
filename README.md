## Currency Exchange Rate Service

A Spring Boot application to manage and retrieve currency exchange rates, with support for PostgreSQL, Flyway migrations, and external API integration (Open Exchange Rates).

---

### Features

- Fetch real-time currency rates from [Open Exchange Rates](https://openexchangerates.org)
- Persist exchange rates into a PostgreSQL database
- Indexing support for performance (by currency code)
- Scheduled background fetch every 1 hour (`@Scheduled`)
- REST API to query currency rates
- Integrated Flyway for schema versioning
- Docker and Docker Compose ready

---

### Tech Stack

- **Java 17+**
- **Spring Boot 3.4+**
- **PostgreSQL 15+**
- **Flyway 10+**
- **Docker & Docker Compose**
- **HikariCP (connection pool)**

---

### Configuration

### `application.yml`

```yaml
server:
  port: 8080
  servlet:
    context-path: /

spring:
  application:
    name: currency-exchange-service
  datasource:
    url: jdbc:postgresql://localhost:5432/exchange_db
    username: postgres
    password: postgres
    hikari:
      driver-class-name: org.postgresql.Driver
      pool-name: HikariCP
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 30000
      connection-timeout: 30000
      max-lifetime: 1800000
      connection-test-query: SELECT 1
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
  flyway:
    enabled: false
    outOfOrder: true
    locations: classpath:db/migration

exchange-rate-api:
  app-id: YOUR_APP_ID
  base-url: https://openexchangerates.org/api
```

### Docker Compose (App + DB)
### `docker-compose.yml`

```agsl
version: "3.8"

services:
  postgres:
    image: postgres:15
    container_name: postgres
    environment:
      POSTGRES_DB: exchange_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  currency-exchange-service:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: currency-exchange-service
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/exchange_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SPRING_JPA_SHOW_SQL: "true"
      SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT: org.hibernate.dialect.PostgreSQLDialect

volumes:
  pgdata:


```
### Start PostgreSQL using Docker

```agsl
docker run --name postgres -e POSTGRES_PASSWORD=postgres -d postgres:15
docker-compose up -d --build

```

### Flyway Migration Example
```sql
src/main/resources/db/migration/V1__Create_exchange_rates_table.sql

CREATE TABLE currency (
    id SERIAL PRIMARY KEY,
    code VARCHAR(3) UNIQUE NOT NULL
);

CREATE TABLE exchange_rate (
    id BIGSERIAL NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    rate NUMERIC NOT NULL,
    PRIMARY KEY (id, timestamp)
) ;

-- Indexes
CREATE INDEX idx_currency_code ON exchange_rate(currency_code);
```
### Run in the background
```
docker-compose up -d --build
```

### Run the Spring Boot app
```
./mvnw spring-boot:run 
```
### Running Tests

```
./mvnw test
```

### API Endpoints
```
Swagger UI: http://localhost:8080/swagger-ui.html
```
### Save Currency
```
POST: /currencies
Example: POST: /currencies
Payload:
{
    "currencyCode": "BDT"
}
```
### Get All Currencies
```
GET: /currencies
Example: GET: /currencies
```
### Get Exchange Rate by Currency Code
```
GET /currencies/rates/{currencyCode}
Example: GET /currencies/rates/USD
```