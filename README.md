# Project Management

# Backend - Spring Boot API

## Prerequisites
- Java 17 or higher
- Maven 3.8+
- Your preferred IDE (IntelliJ IDEA, Eclipse, VS Code)
- Docker
- Docker Compose

## Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/ovitormota/project_management_backend.git
cd project_management_backend
```

### 2. Start PostgreSQL Database
Navigate to the docker directory and start the container:
```bash
cd docker
docker-compose up -d
```

### 3. Build the project
```bash
mvn clean install
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## Running Tests
```bash
mvn test
```

## Useful Docker Commands
- Stop the database: `docker-compose down`
- View logs: `docker-compose logs`
- Restart containers: `docker-compose restart`
