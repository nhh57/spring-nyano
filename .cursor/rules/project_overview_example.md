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

1. **Domain-Driven Design**:
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

2. **Service Layer Pattern**:
   - Application services for use case coordination
   - Domain services for business logic
   
```java
@Service
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {
    @Autowired
    private TicketDetailRepository ticketDetailRepository;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        return ticketDetailRepository.findById(ticketId).orElse(null);
    }
}
```

3. **Repository Pattern**:
   - Interface in domain layer
   - Implementation in infrastructure
   
```java
@Repository
public interface TicketDetailJPAMapper extends JpaRepository<TicketDetail, Long> {
    Optional<TicketDetail> findById(Long id);
}
```

4. **Caching Strategy**:
   - Multi-level caching (Local + Distributed)
   - Cache invalidation through pub/sub
   
```java
@Service
public class TicketDetailCacheService {
    private static final Cache<Long, TicketDetail> ticketDetailLocalCache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .concurrencyLevel(7)
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Autowired
    private RedisInfrasService redisInfrasService;
    
    public TicketDetail getTicketDetail(Long id) {
        // Check local cache
        TicketDetail ticketDetail = getTicketDetailLocalCache(id);
        if (ticketDetail != null) {
            return ticketDetail;
        }
        
        // Check distributed cache
        ticketDetail = redisInfrasService.getObject(genEventItemKey(id), TicketDetail.class);
        if (ticketDetail != null) {
            ticketDetailLocalCache.put(id, ticketDetail);
            return ticketDetail;
        }
        
        // Get from database
        ticketDetail = ticketDetailDomainService.getTicketDetailById(id);
        if (ticketDetail != null) {
            redisInfrasService.setObject(genEventItemKey(id), ticketDetail);
            ticketDetailLocalCache.put(id, ticketDetail);
        }
        
        return ticketDetail;
    }
}
```

5. **Distributed Locking**:
   - Redis-based distributed locks
   - Lock management for concurrent operations
   
```java
@Service
public class RedisDistributedLockerImpl implements RedisDistributedService {
    @Resource
    private RedissonClient redissonClient;

    @Override
    public RedisDistributedLocker getDistributedLock(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);
        return new RedisDistributedLocker() {
            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                return rLock.tryLock(waitTime, leaseTime, unit);
            }
            
            @Override
            public void unlock() {
                if (isLocked() && isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        };
    }
}
```

## Core Domain Models

1. **Ticket Management**:
   - `TicketDetail` entity with audit fields
   - Repository and service implementations
   
```java
@Entity
@Table(name = "ticket_details")
public class TicketDetail extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column
    private String description;
    
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
```

## Infrastructure Highlights

1. **Persistence**:
   - Spring Data JPA with PostgreSQL
   - Repository pattern implementation
   
```java
@Repository
public interface TicketDetailJPAMapper extends JpaRepository<TicketDetail, Long> {
    Optional<TicketDetail> findById(Long id);
}
```

2. **Caching**:
   - Multi-level caching strategy
   - Redis for distributed cache
   - Local cache for performance
   
```java
@Service
public class RedisInfrasServiceImpl implements RedisInfrasService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public void setObject(String key, Object value) {
        if (StringUtils.hasLength(key)) {
            redisTemplate.opsForValue().set(key, value);
        }
    }
    
    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        // Conversion logic
    }
}
```

3. **Distributed Locking**:
   - Redis-based distributed locks
   - Lock management for concurrent operations
   
```java
@Service
public class DistributedLockService {
    public boolean acquireLock(String lockKey, long leaseTime, TimeUnit unit) {
        // Lock acquisition logic
    }
    
    public void releaseLock(String lockKey) {
        // Lock release logic
    }
}
```

## Key Flows

1. **Ticket Processing**:
   ```
   Controller → Application Service → Domain Service → Repository
   ```

2. **Caching Strategy**:
   ```
   Local Cache → Distributed Cache → Database
   ```

3. **Concurrent Operations**:
   ```
   Acquire Lock → Process Operation → Release Lock
   ```

## Getting Started Tips

1. Start with Domain models to understand business rules
2. Use Service Layer pattern for business logic
3. Implement proper caching strategy
4. Handle concurrent operations with distributed locks
5. Follow Clean Architecture principles

## Important Dependencies
- Spring Boot
- Spring Data JPA
- Spring Cache
- Redis
- PostgreSQL
- Redisson

This structure provides a maintainable foundation for a ticket management system with robust caching, concurrency control, and domain-driven design.