# Library Management API

[![unit-tests](https://github.com/lonecalvary78/library-app/actions/workflows/unit-tests.yaml/badge.svg)](https://github.com/lonecalvary78/library-app/actions/workflows/unit-tests.yaml)

[![build-application](https://github.com/lonecalvary78/library-app/actions/workflows/build-application.yaml/badge.svg)](https://github.com/lonecalvary78/library-app/actions/workflows/build-application.yaml)

[![deploy-application](https://github.com/lonecalvary78/library-app/actions/workflows/deploy-application.yaml/badge.svg)](https://github.com/lonecalvary78/library-app/actions/workflows/deploy-application.yaml)

A RESTful API that manages a simple library system

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 17
- Docker

## Technologies Used

- Spring Boot 3.4.5
- Spring Data JPA
- Spring Validation
- PostgreSQL (for both development and production)
- Flyway (for database migrations)
- Lombok
- Springdoc OpenAPI (for API documentation)
- TestContainers (for integration testing with real PostgreSQL)

## Configuration

The application uses YAML configuration files with environment variable overrides:

- `application.yml` - Default configuration
- `application-dev.yml` - Development environment configuration
- `application-prod.yml` - Production environment configuration

All configuration values can be overridden using environment variables.

## Testing

The application includes both unit tests and integration tests:

- Unit tests use Mockito to mock dependencies and focus on testing business logic
- Integration tests use TestContainers to spin up a real PostgreSQL database in Docker containers for testing repository layers with actual database interactions

To run the tests:

```bash
mvn test
```


### OpenAPI Specification

The application includes a comprehensive OpenAPI 3.0 specification that documents all available endpoints, request/response structures, and possible error responses. The specification provides:

- Detailed descriptions for each API endpoint
- Request and response schemas with examples
- Required vs. optional fields
- Possible error responses and their formats
- Authentication requirements

The OpenAPI specification can be accessed in different formats:
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- OpenAPI YAML: [http://localhost:8080/v3/api-docs.yaml](http://localhost:8080/v3/api-docs.yaml)

## API Endpoints

### Borrowers

- **Register a new borrower**: `POST /api/borrowers`
- **Get all borrowers**: `GET /api/borrowers`
- **Get a borrower by ID**: `GET /api/borrowers/{id}`

### Books

- **Register a new book**: `POST /api/books`
- **Get all books**: `GET /api/books`
- **Get a book by ID**: `GET /api/books/{id}`
- **Borrow a book**: `PUT /api/books/{id}/borrow`
- **Return a book**: `PUT /api/books/{id}/return`

## Error Handling

The API includes comprehensive error handling with appropriate HTTP status codes:

- 400 Bad Request: For validation errors
- 404 Not Found: When a requested resource doesn't exist
- 409 Conflict: When attempting operations that conflict with existing data
- 500 Internal Server Error: For unexpected server errors 