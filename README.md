# http://www.deepcatch.eu

# Fishing Log Tracker Backend

A robust Spring Boot backend service for tracking fishing activities, built with Spring Boot 3.2.3.

## Features

- RESTful API with comprehensive endpoints
- JWT-based authentication and authorization
- PostgreSQL database with JPA
- Real-time features using WebSocket
- AWS S3 integration for image storage
- OpenAPI/Swagger documentation
- Caching support
- Object mapping with MapStruct
- Input validation
- Exception handling
- CORS configuration

## Tech Stack

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (jjwt 0.11.5)
- WebSocket
- AWS SDK 2.31.47
- MapStruct 1.5.5
- Lombok
- SpringDoc OpenAPI 2.5.0
- Spring Cache
- Spring Validation

## Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- AWS Account (for S3 functionality)

## Installation

1. Clone the repository
2. Navigate to the project directory:
   ```bash
   cd fishingLogTracker
   ```
3. Install dependencies:
   ```bash
   mvn install
   ```

## Configuration

Create an `application.properties` or `application.yml` file with the following configurations:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/fishing_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your_jwt_secret
jwt.expiration=86400000

# AWS Configuration
aws.accessKeyId=your_access_key
aws.secretKey=your_secret_key
aws.region=your_region
aws.s3.bucket=your_bucket_name

# WebSocket Configuration
websocket.allowed-origins=http://localhost:4200
```

## Running the Application

To run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Documentation

Once the application is running, you can access the Swagger documentation at:

```
http://localhost:8080/swagger-ui.html
```

## Docker Support

The project includes a Dockerfile for containerization. To build and run the Docker container:

```bash
docker build -t fishing-backend .
docker run -p 8080:8080 fishing-backend
```

## Project Structure

- `src/main/java/finalProject/fishingLogTracker/`
  - `auth/` - Authentication and authorization
  - `config/` - Application configuration
  - `fishingTracker/` - Core business logic
    - `controller/` - REST API endpoints
    - `service/` - Business logic implementation
    - `repository/` - Data access layer
    - `entity/` - Database entities
    - `dto/` - Data transfer objects
    - `mapper/` - Object mapping (MapStruct)
    - `enums/` - Java enums
    - `exception/` - Custom exceptions
    - `config/` - Module-specific configuration
  - `websockets/` - WebSocket implementation
  - `FishingLogTrackerApplication.java` - Application entry point

## Testing

Run the tests using:

```bash
mvn test
```

The project includes:

- Unit tests
- Integration tests

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
