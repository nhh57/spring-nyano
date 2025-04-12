# Ngữ cảnh Kỹ thuật

## Môi trường Phát triển
- Java 21 JDK
- Maven 3.x
- Docker
- Môi trường Linux (Ubuntu 6.11.0-21-generic)

## Dependencies Cốt lõi
```xml
- Spring Boot 3.3.5
- Resilience4j-spring-boot3 2.1.0
- Guava 32.1.2-jre
- Logstash-logback-encoder 8.0
- Lombok
```

## Cấu trúc Dự án
```
spring-nyano/
├── nyano-start/         # Điểm vào ứng dụng
├── nyano-controller/    # REST controllers
├── nyano-application/   # Dịch vụ ứng dụng
├── nyano-domain/       # Mô hình domain và logic
├── nyano-infrastructure/# Tích hợp bên ngoài
├── benchmark/          # Tệp kiểm tra hiệu suất
└── documentations/     # Tài liệu dự án
```

## Build và Chạy
1. Build:
   ```bash
   mvn clean install
   ```

2. Chạy:
   ```bash
   java -jar nyano-start/target/nyano-start.jar
   ```

3. Docker:
   ```bash
   docker build -t spring-nyano .
   docker run -p 8080:8080 spring-nyano
   ```

## Kiểm tra Hiệu suất
Sử dụng Vegeta cho kiểm tra tải:
```bash
vegeta attack -targets=targets.txt -duration=10s -rate=100
```

## Giám sát
- Spring Actuator endpoints
- Logstash logging
- Số liệu Resilience4j

## Công cụ Phát triển
1. Hỗ trợ IDE
   - IntelliJ IDEA
   - VSCode với các tiện ích Java

2. Công cụ Kiểm tra
   - Vegeta cho kiểm tra tải
   - Postman cho kiểm tra API

3. Công cụ Build
   - Maven
   - Docker

## Ràng buộc Kỹ thuật
1. Phiên bản Java: 21+
2. Spring Boot: 3.3.x
3. Bộ nhớ: Dựa trên giới hạn container
4. Cổng: Có thể cấu hình, mặc định 8080 