# 🔗 Shortner Link

> A modern and efficient URL shortening service built with Spring Boot

![Project Banner](./assets/banner.png)

## 📋 Table of Contents

-   [About the Project](#-about-the-project)
-   [Features](#-features)
-   [Technologies Used](#-technologies-used)
-   [Architecture](#-architecture)
-   [Prerequisites](#-prerequisites)
-   [Installation and Setup](#-installation-and-setup)
-   [API Endpoints](#-api-endpoints)
-   [Usage Examples](#-usage-examples)
-   [Project Structure](#-project-structure)
-   [Configuration](#-configuration)
-   [Logging](#-logging)
-   [Contributing](#-contributing)
-   [License](#-license)

## 🎯 About the Project

**Shortner Link** is a Java application built on Spring Boot that provides a complete URL shortening service. The project was developed as a Proof of Concept (PoC) by BortolettoTech, demonstrating best development practices with modern technologies.

### Why use this service?

-   ✅ **Simplicity**: Clean and intuitive REST API interface
-   ✅ **Security**: Unique code generation using SHA-256
-   ✅ **Persistence**: Reliable storage with MongoDB
-   ✅ **Flexibility**: Support for multiple notification types
-   ✅ **Traceability**: Complete logging system
-   ✅ **Containerization**: Production-ready with Docker

## 🚀 Features

### Core Features

-   **URL Shortening**: Converts long URLs into short and unique codes
-   **Redirection**: Automatically redirects to the original URL
-   **URL Validation**: Automatically adds `https://` when needed
-   **Duplicate Prevention**: Returns the same code for already shortened URLs
-   **Link Deletion**: Removes links from the system when necessary

### Advanced Features

-   **Notification Types**: Support for EMAIL, WHATSAPP and OTHER
-   **Structured Logging**: Complete logging system with Log4j2
-   **Health Checks**: Application health monitoring
-   **API Documentation**: Integrated Swagger/OpenAPI interface

## 🛠 Technologies Used

### Backend

-   **Java 17** - Programming language
-   **Spring Boot 2.7.3** - Main framework
-   **Spring Data MongoDB** - MongoDB integration
-   **Spring Web** - REST API

### Database

-   **MongoDB 6.0** - NoSQL database

### Tools

-   **Maven** - Dependency management
-   **Docker & Docker Compose** - Containerization
-   **Lombok** - Boilerplate reduction
-   **Log4j2** - Logging system
-   **SLF4J** - Logging interface

### Code Quality

-   **JUnit** - Unit testing
-   **SonarQube** - Quality analysis

## 🏗 Architecture

```mermaid
graph TD
    A[Controller<br/>REST API] --> B[Service<br/>Business Logic]
    B --> C[Repository<br/>Data Access]

    A --> D[DTO<br/>Data Transfer]
    B --> E[Model<br/>Entity]
    C --> F[MongoDB<br/>Database]

    style A fill:#e1f5fe
    style B fill:#f3e5f5
    style C fill:#e8f5e8
    style D fill:#fff3e0
    style E fill:#fce4ec
    style F fill:#f1f8e9
```

### Data Flow

1. **Request**: Client sends URL to shorten
2. **Validation**: System validates and formats the URL
3. **Generation**: Generates unique code using SHA-256
4. **Storage**: Stores in MongoDB
5. **Response**: Returns shortened code

## 📋 Prerequisites

Before you begin, make sure you have installed:

-   **Java 17+** ([Download](https://adoptium.net/))
-   **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
-   **Docker & Docker Compose** ([Download](https://docs.docker.com/get-docker/))
-   **Git** ([Download](https://git-scm.com/downloads))

## 🚀 Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/bortolettojoaog/shortner-link.git
cd shortner-link
```

### 2. Set up the Database

```bash
# Start MongoDB with Docker Compose
docker-compose up -d
```

### 3. Run the Application

```bash
# Compile and run
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### 4. Verify the Setup

The application will be available at: `http://localhost:8080`

## 📡 API Endpoints

### Base URL

```
http://localhost:8080
```

### Available Endpoints

#### 1. Create Shortened Link

```http
POST /shortner-link
Content-Type: application/json

{
  "originalUrl": "https://www.example.com/very-long-url",
  "username": "user123",
  "notificationType": "EMAIL"
}
```

**Response:**

```json
{
    "shortCode": "a1b2c3d4e5"
}
```

#### 2. Redirect

```http
GET /{shortCode}
```

Automatically redirects to the original URL.

#### 3. Delete Link

```http
DELETE /?shortCode={shortCode}
```

**Response:**

```json
{
    "message": "Link deleted successfully"
}
```

### Status Codes

-   `200` - Success
-   `400` - Bad request
-   `404` - Link not found
-   `429` - Monthly link limit reached (50 active links max; oldest links expire after 30 days)
-   `500` - Internal server error

## 💡 Usage Examples

### Using cURL

#### Create a shortened link:

```bash
curl -X POST http://localhost:8080/shortner-link \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://github.com/bortolettojoaog/shortner-link",
    "username": "developer",
    "notificationType": "EMAIL"
  }'
```

#### Access shortened link:

```bash
curl -L http://localhost:8080/a1b2c3d4e5
```

#### Delete link:

```bash
curl -X DELETE "http://localhost:8080/?shortCode=a1b2c3d4e5"
```

### Using JavaScript/Fetch

```javascript
// Create shortened link
const response = await fetch('http://localhost:8080/shortner-link', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        originalUrl: 'https://example.com',
        username: 'user',
        notificationType: 'WHATSAPP',
    }),
});

const result = await response.json();
console.log('Short code:', result.shortCode);
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/bortolettotech/shortner_link/
│   │   ├── ShortnerLinkApplication.java    # Main class
│   │   ├── controller/
│   │   │   └── LinkController.java         # REST endpoints
│   │   ├── dto/
│   │   │   └── LinkDTO.java               # Data Transfer Object
│   │   ├── model/
│   │   │   └── Link.java                  # MongoDB entity
│   │   ├── repository/
│   │   │   └── LinkRepositoryI.java       # Repository interface
│   │   ├── service/
│   │   │   └── LinkService.java           # Business logic
│   │   └── type/
│   │       └── NotificationType.java      # Type enum
│   └── resources/
│       ├── application.properties          # Configuration
│       └── log4j2.yaml                    # Logging configuration
└── test/
    └── java/com/bortolettotech/shortner_link/
        └── ShortnerLinkApplicationTests.java
```

## ⚙️ Configuration

### application.properties

```properties
# Application settings
spring.application.name=shortner-link
spring.port=8080

# MongoDB settings
spring.data.mongodb.uri=mongodb://root:example@localhost:27017
spring.data.mongodb.database=shortner-link

# Logging settings
logging.config=classpath:log4j2.yaml
```

### Environment Variables

You can override the configurations using environment variables:

```bash
export SPRING_DATA_MONGODB_URI=mongodb://your-mongodb-uri
export SPRING_PORT=8080
export SPRING_DATA_MONGODB_DATABASE=your-database
```

## 📊 Logging

The system has structured logging configured with Log4j2:

-   **Location**: `logs/shortner-link.log`
-   **Rotation**: Automatic by size and date
-   **Levels**: DEBUG, INFO, WARN, ERROR

### Log Example

```
2025-07-23 10:30:45.123 [http-nio-8080-exec-1] INFO  c.x.s.controller.LinkController - Received request to generate short link: LinkDTO(originalUrl=https://example.com, username=user, notificationType=EMAIL)
```

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the project
2. Create a branch for your feature (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Open a Pull Request

### Development Standards

-   Use Java naming conventions
-   Add tests for new features
-   Keep code documented
-   Follow Spring Boot best practices

## 📄 License

This project is under the MIT license. See the [LICENSE](LICENSE) file for more details.

---

<div align="center">
  <p>Developed with ❤️ by <strong>BortolettoTech</strong></p>
  <p>⭐ If this project was helpful, consider giving it a star!</p>
</div>
