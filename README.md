# Hotel Booking Microservices

Полная реализация учебного проекта REST API системы бронирования отелей на Spring Boot (микросервисная архитектура).

## Состав проекта
- `eureka-server` — Service Discovery (Eureka).
- `api-gateway` — маршрутизация запросов через Spring Cloud Gateway.
- `booking-service` — пользователи, JWT, бронирования, двухшаговое подтверждение с компенсацией.
- `hotel-service` — отели, номера, подтверждение доступности, release блокировок, рекомендации по `times_booked`.

## Технологии
- Java 17
- Spring Boot 3.3.x
- Spring Cloud 2023.0.x
- Spring Security + JWT (HS256)
- Spring Data JPA + H2
- Spring Retry
- Springdoc OpenAPI
- JUnit 5

## Быстрый запуск
```bash
mvn clean test
```

Запуск сервисов в отдельных терминалах:
```bash
mvn -pl eureka-server spring-boot:run
mvn -pl hotel-service spring-boot:run
mvn -pl booking-service spring-boot:run
mvn -pl api-gateway spring-boot:run
```

## Порты
- Eureka: `8761`
- Gateway: `8080`
- Booking: `8081`
- Hotel: `8082`

## JWT и роли
- TTL токена: 1 час.
- Роли: `ROLE_USER`, `ROLE_ADMIN`.
- Получение токена:
  - `POST /user/register`
  - `POST /user/auth`

Предзаполненные пользователи (booking-service):
- `admin / admin123` (ADMIN)
- `user / user123` (USER)

## Основные эндпойнты
### Booking Service (через Gateway)
- `POST /user/register`
- `POST /user/auth`
- `POST /user` (ADMIN)
- `PATCH /user` (ADMIN)
- `DELETE /user?username=...` (ADMIN)
- `POST /booking` (USER)
- `GET /bookings` (USER)
- `GET /booking/{id}` (USER)
- `DELETE /booking/{id}` (USER)

### Hotel Service
- `POST /api/hotels` (ADMIN)
- `GET /api/hotels` (USER/ADMIN)
- `POST /api/rooms` (ADMIN)
- `GET /api/rooms` (USER/ADMIN)
- `GET /api/rooms/recommend` (USER/ADMIN)
- `POST /internal/rooms/{id}/confirm-availability` (internal)
- `POST /internal/rooms/{id}/release?requestId=...` (internal)

## Реализация саги бронирования
1. Booking service создаёт запись `PENDING`.
2. Вызывает Hotel service `confirm-availability` (с `requestId`, retry + backoff + timeout).
3. При успехе -> `CONFIRMED`.
4. При ошибке/тайм-ауте -> `CANCELLED` и компенсация `release`.

## Идемпотентность
- В booking-service `requestId` уникален (`bookings.request_id`).
- В hotel-service уникальная пара (`room_id`, `request_id`) для lock.

## Swagger
- Booking: `http://localhost:8081/swagger-ui/index.html`
- Hotel: `http://localhost:8082/swagger-ui/index.html`

## Тесты
- `hotel-service`: проверка конфликтующих пересекающихся блокировок.
- `booking-service`: smoke тест регистрации с получением JWT.
