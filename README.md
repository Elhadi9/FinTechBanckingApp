# FinTech Banking App

A secure, full-featured banking application built with **Java**, **Spring Boot**, **Spring Security**, **MySQL**, and **Thymeleaf**, with role-based access (users & admins) and Docker support for easy setup and deployment.

---

##  Features

- **Role-based authentication & authorization**
  - `ADMIN`: manage users, accounts, view stats, update statuses
  - `USER`: perform deposits, withdrawals, transfers, view own accounts
- **User-managed accounts**
  - Create, delete (with safety checks), and manipulate accounts
- **Account operations**
  - Deposit, withdraw, transfer between own accounts
  - Prevent deleting sole account or deleting with remaining balance without transfer
- **Admin dashboard**
  - View system stats: total users, total accounts, suspended ones
  - Management UI to list and filter users/accounts
- **Persistent storage using MySQL**
- **Rich frontend with Thymeleaf templating**
- **Logging and detailed error traces enabled**
- **Environment-specific configs** (e.g., `application.properties`)
- **Dockerized**: containerized Spring Boot app with embedded Tomcat + MySQL

---

##  Tech Stack & Requirements

| Component          | Version / Info                 |
|------------------|-------------------------------|
| Java              | 17 (or compatible JDK)         |
| Spring Boot       | 3.x                             |
| Spring Security   | Included via starter           |
| Thymeleaf         | Included via starter           |
| Database          | MySQL 8.x                      |
| Build Tool       | Maven                         |
| Optional Containerization | Docker & Docker Compose        |

---

##  Setup & Running the Project

### Option 1: Run Locally with Maven (without Docker)

1. **Configure your local MySQL** and update `src/main/resources/application.properties`:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/bankdb
    spring.datasource.username=root
    spring.datasource.password=MySecurePass123!@#
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
    spring.jpa.hibernate.ddl-auto=update

    server.port=8081
    logging.level.org.springframework=DEBUG
    logging.level.com.bank=DEBUG
    server.error.include-stacktrace=always
    server.error.include-message=always
    ```

2. Build and run using Maven:

    ```bash
    mvn clean package
    mvn spring-boot:run
    ```

3. Open your browser at:  
    **http://localhost:8081**

---

### Option 2: Run with Docker & Docker Compose

####  Dockerfile (in project root
mvn clean package -DskipTests
docker-compose up --build
