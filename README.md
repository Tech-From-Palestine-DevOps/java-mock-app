# Factorial Calculator

A Spring Boot application that performs factorial calculations with response time tracking and environment-specific configurations. The application provides real-time performance metrics and a modern web interface.

![main_gif](./docs/gif/main.gif)

## Sequence diagram
```mermaid
sequenceDiagram
    participant User as User (You)
    participant Browser as Browser (Front End)
    participant Server as Spring Boot Server (Back End)
    
    User->>Browser: Enter app URL (e.g., localhost:8080)
    Browser->>Server: Request Homepage (GET /)
    Server-->>Browser: Sends HTML/CSS for Calculator page
    Browser-->>User: Displays Calculator UI

    User->>Browser: Enter numbers, click "Calculate"
    Browser->>Server: Send numbers (POST /factorial)

    %% Server processes the calculation
    alt Connection is valid
        Server-->>Browser: Responds with result (new HTML with answer)
        Browser-->>User: Shows calculation result
    else Connection is invalid
        Server-->>Browser: Responds with error ("Connection invalid")
        Browser-->>User: Shows error message
    end

    Note over User,Browser: User can repeat calculation steps
```


## Configuration

| Property | Description | Default | Env Variable | Example |
|----------|-------------|---------|--------------|---------|
| `app.name` | Application name | - | `APP_NAME` | `Factorial Calculator` |
| `app.env` | Current environment | `dev` | `APP_ENV` | `dev`, `tst`, `acc`, `prod` |
| `app.author` | Application author | - | `APP_AUTHOR` | `John Doe` |
| `app.version` | Application version | - | `APP_VERSION` | `1.0.0` |
| `app.resources.cpu` | CPU resources for response time | `1` | `APP_RESOURCES_CPU` | `2`, `4`, `8` |
| `app.factorial.strategy` | Factorial calculation strategy | `iterative` | `APP_FACTORIAL_STRATEGY` | `iterative`, `recursive` |
| `app.env.tokens.dev` | Dev environment token | - | - | `dev-token-123` |
| `app.env.tokens.tst` | Test environment token | - | - | `test-token-456` |
| `app.env.tokens.acc` | Acceptance environment token | - | - | `acc-token-789` |
| `app.env.tokens.prod` | Production environment token | - | - | `prod-token-012` |


## Running Tests

```bash
# Run all tests
mvn test

# Run integration tests
mvn verify

# Run specific test
mvn test -Dtest=TokenConfigIT
```

## Features

- Fast factorial calculations (up to 20!)
- Response time tracking and performance metrics
- Environment-specific configurations
- Modern Material Design interface
- Memory-optimized (200MB limit)

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- Spring Boot 3.x

## Building and Running

```bash
# Build
mvn clean package

# Run
java -Xmx200m -Xms200m -jar target/factorial-calculator-0.0.1-SNAPSHOT.jar
# or
mvn spring-boot:run
```

Note: Environment variables take precedence over properties file values.

## Project Structure

- `src/main/java/com/techfrompalestine/factorialcalculator/`
  - `config/TokenConfig.java` - Connection configuration and validation logic
  - `controller/FactorialController.java` - Web controller for factorial calculations
  - `service/` - Business logic services
    - `CalculationService.java` - Factorial calculation service
    - `ColorService.java` - Material Design color generation
    - `ResponseTimeService.java` - Performance tracking
    - `ModelAttributeService.java` - View model management
  - `strategy/` - Factorial calculation strategies
    - `FactorialStrategy.java` - Strategy interface
    - `IterativeFactorialStrategy.java` - Iterative implementation
    - `RecursiveFactorialStrategy.java` - Recursive implementation
  - `util/` - Utility classes
    - `FactorialValidator.java` - Input validation
    - `LogCalcUtil.java` - Calculation logging
  - `FactorialCalculatorApplication.java` - Main application class
- `src/main/resources/`
  - `application.properties` - Application configuration
  - `templates/index.html` - Web interface
- `src/test/` - Unit tests

## Features

- Factorial calculations with iterative and recursive strategies
- Response time tracking
- Environment status
- Material Design UI

## License

This project is licensed under the MIT License. 