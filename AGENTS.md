# AGENTS.md - Book App Developer Guide

## Project Overview

**Book App** is a Spring Boot 4.0.2 REST API for managing books, built with Java 21, MySQL, and Spring Data JPA. The application follows a classic three-tier layered architecture: Controller → Service → Repository.

## Key Architecture Pattern

```
HTTP Request → BookController (/api/books)
    ↓
BookService (business logic)
    ↓
BookRepository (JPA interface)
    ↓
MySQL Database (bookdb)
```

**Pattern Details:**
- **Controller** (`BookController`): REST endpoints with `@RestController`, uses constructor injection for `BookService`
- **Service** (`BookService`): Contains CRUD logic, directly delegates to repository with null-safety checks
- **Repository** (`BookRepository`): Extends `JpaRepository<Book, Integer>` - provides out-of-box CRUD methods
- **Model** (`Book`): JPA entity with `@Entity`, mapped to `books` table; note the mismatch between field name `bookId` and getter `getId()` 

**Critical Issue to Know:** The `Book.java` file has a bug - field is named `bookId` but constructors/getters use `id`. This causes potential NPE in entity instantiation.

## Database & Configuration

- **MySQL Container**: Defined in `docker-compose.yml`
  - Database: `bookdb`, User: `root`, Password: `root`, Port: `3306`
  - Persistent volume: `./db_data:/var/lib/mysql`
  - Start with: `docker-compose up -d`

- **Spring Configuration** (`application.properties`):
  - JPA DDL auto is set to `update` (migrations via schema auto-update)
  - SQL logging enabled via `spring.jpa.show-sql=true`
  - Connect locally via JDBC connection string pointing to `localhost:3306`

## Build & Development Workflows

- **Build**: `mvn clean package` or use Maven wrapper `./mvnw clean package`
- **Run**: `mvn spring-boot:run` or `./mvnw spring-boot:run`
- **Tests**: `mvn test` - dependencies include `spring-boot-starter-data-jpa-test` and `spring-boot-starter-webmvc-test`
- **Clean Target**: `mvn clean` removes compiled classes in `/target`

**Prerequisites:** Docker running with MySQL container from `docker-compose.yml` before running the app.

## API Endpoints

All routes prefixed with `/api/books`:

| Method | Endpoint | Service Method |
|--------|----------|-----------------|
| `POST` | `/` | `createBook(Book)` |
| `GET` | `/` | `getAllBooks()` |
| `GET` | `/{id}` | `getBookById(Integer)` |
| `PUT` | `/{id}` | `updateBook(Integer, Book)` |
| `DELETE` | `/{id}` | `deleteBook(Integer)` |

**Request/Response Body** uses `Book` model with fields: `bookId` (auto-generated), `title`, `author`, `price` (mapped to column `cost`).

## Code Conventions & Patterns

1. **Constructor Injection**: All dependencies use constructor parameters marked as `final`, avoiding `@Autowired` field injection
   ```java
   private final BookService bookService;
   public BookController(BookService bookService) {
       this.bookService = bookService;
   }
   ```

2. **Method Naming**: Service layer prefixes operations clearly (`createBook`, `getAllBooks`, `getBookById`, etc.)

3. **Null Handling**: Service methods return `null` on not-found; controller doesn't wrap responses in `Optional` or `ResponseEntity`
   ```java
   return bookRepository.findById(id).orElse(null);
   ```

4. **Entity Mapping**: Use `@Column(name = "cost")` to map fields to different DB column names (e.g., `price` field → `cost` column)

## Common Development Tasks

**Adding a new endpoint:**
1. Add method to `BookService` with business logic
2. Add `@RequestMapping` method in `BookController` (e.g., `@PostMapping`, `@GetMapping`)
3. For complex queries, add methods to `BookRepository` interface - Spring Data JPA generates implementations via method naming conventions

**Modifying the Book model:**
1. Edit `Book.java` entity
2. Spring's `ddl-auto=update` will auto-migrate schema (careful in production - use proper migrations instead)
3. Update service methods if field logic changes (e.g., `updateBook()`)

**Debugging:**
- Enable SQL logging is already on; check console output for Hibernate SQL queries
- Ensure MySQL container is running: `docker ps | findstr bookdb_container` (Windows)
- Check database state directly with MySQL client or tools like DBeaver

## Known Issues & Workarounds

1. **Book Entity Field Mismatch**: Constructor parameter is `id` but field is `bookId`. Constructors incorrectly set `this.id = id;` (undefined field). This will fail at runtime if the constructor is called.
   - **Workaround**: Use parameterless constructor or ensure only repository creates entities

2. **No Error Handling**: Service returns `null` for missing records; controllers don't handle this, resulting in `null` JSON responses
   - **Pattern**: Consider wrapping responses in `ResponseEntity<?>` with proper HTTP status codes

3. **No Input Validation**: No `@Valid` annotations on `@RequestBody` parameters; malformed requests may cause 500 errors
   - **Pattern**: Add `@Valid` and use `@NotNull`, `@NotBlank` annotations

## Dependencies (from pom.xml)

- `spring-boot-starter-data-jpa`: JPA/Hibernate ORM
- `spring-boot-starter-webmvc`: Spring MVC and embedded Tomcat
- `mysql-connector-j`: MySQL JDBC driver
- **Test scope**: JPA and WebMVC testing starters

**Java Version**: 21 (modern LTS, uses jakarta.persistence instead of javax.persistence)

## Project Structure

```
src/main/java/com/vvit/bookapp/
├── BookAppApplication.java       # Spring Boot entry point
├── controller/BookController.java # REST endpoints
├── service/BookService.java       # Business logic
├── model/Book.java                # JPA entity
└── repository/BookRepository.java # Data access interface

src/main/resources/
├── application.properties          # Database and Spring config
├── static/                        # Static assets (CSS, JS, images)
└── templates/                     # Thymeleaf templates (if needed)
```

## Quick Reference Commands

```bash
# Start MySQL container
docker-compose up -d

# Build the application
./mvnw clean package

# Run the application
./mvnw spring-boot:run

# Run tests
./mvnw test

# Check if MySQL is running
docker ps | findstr bookdb_container

# View logs
docker logs -f bookdb_container
```

---
**Last Updated**: Spring Boot 4.0.2, Java 21, MySQL 8.0+

