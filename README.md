# Token Validator Application

A simple Spring Boot application that validates a token by comparing it with a hardcoded base64-encoded value.

## Features

- Token validation through a simple web interface
- Visual feedback (green/red) based on token validity
- Base64-encoded token comparison
- Unit tests for validation logic

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd token-validator
```

2. Configure the token in `src/main/resources/application.properties`:
```properties
app.token=your-token-here
```

## Building and Running

Build the application:
```bash
mvn clean install
```

Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Testing

Run the tests:
```bash
mvn test
```

## Project Structure

- `src/main/java/com/example/tokenvalidator/`
  - `config/TokenConfig.java` - Token configuration and validation logic
  - `controller/TokenController.java` - Web controller for token validation
  - `TokenValidatorApplication.java` - Main application class
- `src/main/resources/`
  - `application.properties` - Application configuration
  - `templates/index.html` - Web interface
- `src/test/` - Unit tests

## How It Works

1. The application reads a token from `application.properties`
2. It compares this token with a hardcoded base64-encoded value
3. The web interface shows:
   - Green button when the token is valid
   - Red button when the token is invalid

## License

This project is licensed under the MIT License. 