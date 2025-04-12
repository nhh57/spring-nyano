# Spring Nyano Project Overview

## Project Structure
The project follows Clean Architecture principles with clear separation of concerns:

```
spring-nyano/
├── nyano-start/         - Application startup and configuration
│   ├── src/main/java/   - Main source code
│   ├── src/main/resources/ - Configuration files
│   └── src/test/        - Unit tests
├── nyano-controller/    - REST API and controllers
│   ├── src/main/java/   - Controllers, DTOs, validators
│   └── src/test/        - Controller tests
├── nyano-application/   - Business logic and use cases
│   ├── src/main/java/   - Services, command handlers
│   └── src/test/        - Service tests
├── nyano-domain/        - Core domain models and business rules
│   ├── src/main/java/   - Entities, value objects, repositories
│   └── src/test/        - Domain tests
└── nyano-infrastructure/- Technical concerns
    ├── src/main/java/   - Persistence, messaging, caching
    └── src/test/        - Infrastructure tests
```

## Key Patterns & Concepts

1. **CQRS Pattern**:
   - Commands and Queries are separated
   - Spring's Command Pattern is used for command handling
   
```java
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CreateCategoryCommand command) {
        return ResponseEntity.ok(categoryService.createCategory(command));
    }
}

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    
    public CategoryDTO createCategory(CreateCategoryCommand command) {
        // Business logic here
    }
}
```

2. **Domain-Driven Design**:
   - Rich domain models with encapsulated business logic
   - Value Objects for type safety:
   
```java
@Embeddable
public class DataType {
    private String value;
    
    private DataType(String value) {
        this.value = value;
    }
    
    public static final DataType STRING = new DataType("string");
    public static final DataType NUMBER = new DataType("number");
    public static final DataType BOOLEAN = new DataType("boolean");
    
    public static DataType from(String value) {
        return switch (value.toLowerCase()) {
            case "string" -> STRING;
            case "number" -> NUMBER;
            case "boolean" -> BOOLEAN;
            default -> throw new DomainException("Invalid data type: " + value);
        };
    }
}
```

3. **Auditing**:
   - BaseAuditableEntity tracks creation/modification dates
   
```java
@MappedSuperclass
public abstract class BaseAuditableEntity {
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

4. **Circuit Breaker Pattern**:
   - Implements fault tolerance for external services
   - Uses Spring Cloud Circuit Breaker
   
```java
@Service
public class ExternalService {
    private final CircuitBreaker circuitBreaker;
    
    @CircuitBreaker(name = "externalService", fallbackMethod = "fallback")
    public String callExternalService() {
        // Service call logic
    }
    
    private String fallback(Exception e) {
        // Fallback logic
    }
}
```

## Core Domain Models

1. **Configuration Management**:
   - `ConfigurationCategory` > `ConfigurationItem` > `ConfigurationValue` hierarchy
   - Versioning and draft/live states for configuration values
   
```java
@Entity
@Table(name = "configuration_items")
public class ConfigurationItem extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String key;
    
    @Column
    private String description;
    
    @Embedded
    private DataType dataType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ConfigurationCategory category;
    
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ConfigurationValue> values = new ArrayList<>();
    
    // Business methods
    public void update(String key, String description) {
        this.key = key;
        this.description = description;
    }
}
```

2. **Deployment System**:
   - `ConfigCart` with validation states
   - Deployment tracking with audit history
   
```java
@Entity
@Table(name = "deployments")
public class Deployment extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_cart_id")
    private ConfigCart configCart;
    
    public void markAsSuccess() {
        this.status = DeploymentStatus.SUCCESS;
    }
    
    public void markAsFailed() {
        this.status = DeploymentStatus.FAILED;
    }
}
```

## Infrastructure Highlights

1. **Persistence**:
   - Spring Data JPA with PostgreSQL
   - Repository pattern implementation
   
```java
@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationItem, Long> {
    Optional<ConfigurationItem> findByKey(String key);
    
    @Query("SELECT c FROM ConfigurationItem c WHERE c.category.id = :categoryId")
    List<ConfigurationItem> findByCategoryId(Long categoryId);
}
```

2. **Caching**:
   - Spring Cache with Redis
   
```java
@Service
@CacheConfig(cacheNames = "configurations")
public class ConfigurationService {
    @Cacheable(key = "#categoryName + ':' + #key")
    public ConfigurationValue getValue(String categoryName, String key) {
        // Cache miss logic
    }
}
```

3. **Messaging**:
   - Spring AMQP with RabbitMQ
   
```java
@Service
public class DeploymentQueueService {
    private final RabbitTemplate rabbitTemplate;
    
    public void publishDeploymentNotification(DeploymentNotification notification) {
        rabbitTemplate.convertAndSend("deployments", notification);
    }
    
    @RabbitListener(queues = "deployments")
    public void handleDeploymentNotification(DeploymentNotification notification) {
        // Handle notification
    }
}
```

## Key Flows

1. **Configuration Creation**:
   ```
   CreateCategoryCommand → Validation → Repository → Cache Update
   ```

2. **Deployment Workflow**:
   ```
   Create Deployment → Validate Cart → Queue Notification → Store Configurations
   ```

3. **Validation Process**:
   
```java
@Configuration
public class ValidationConfig {
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
}

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException ex) {
        // Handle validation errors
    }
}
```

## Getting Started Tips

1. Start with Domain models to understand business rules
2. Follow the CQRS pattern in Application layer for new features
3. Use Spring Data JPA for data access
4. Leverage existing value objects for type safety
5. Utilize Spring Cloud Circuit Breaker for external service calls

## Important Dependencies
- Spring Boot
- Spring Data JPA
- Spring Cloud
- Spring AMQP
- Spring Cache
- Spring Validation
- PostgreSQL
- Redis
- RabbitMQ

This structure provides a maintainable foundation for a configuration management system with robust auditing, versioning, and deployment capabilities. 