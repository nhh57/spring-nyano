# Project Brief

## Project Overview
- **Project Name**: Spring Nyano
- **Type**: Java/Spring-based Ticket Management System
- **Status**: Active Development
- **Repository**: Private

## Core Requirements
1. **Ticket Management**
   - Create, read, update, and delete tickets
   - Track ticket status and history
   - Support concurrent operations
   - Implement multi-level caching

2. **Performance Requirements**
   - Handle 1000+ tickets per second
   - Support concurrent operations
   - Implement efficient caching strategies
   - Ensure data consistency

3. **Technical Requirements**
   - Java 17
   - Spring Boot 3.x
   - PostgreSQL
   - Redis for caching and distributed locking
   - Multi-level caching (Local + Distributed)

## Project Goals
1. Build a scalable ticket management system
2. Implement robust caching mechanisms
3. Ensure data consistency in concurrent operations
4. Maintain high performance under load
5. Follow clean architecture principles

## Success Criteria
1. System handles 1000+ tickets per second
2. Zero data inconsistencies in concurrent operations
3. Response time < 100ms for 95% of requests
4. 99.9% uptime
5. Comprehensive test coverage (>80%)

## Timeline
- **Start Date**: [Start Date]
- **Current Phase**: Active Development
- **Next Milestone**: Performance Optimization

## Tổng quan
Spring Nyano là một ứng dụng Java Spring Boot dựa trên Domain-Driven Design (DDD) nhằm minh họa các mẫu kiến trúc hiện đại và kỹ thuật tối ưu hiệu suất.

## Yêu cầu Cốt lõi
1. Triển khai kiến trúc mô-đun dựa trên DDD
2. Cung cấp REST API hiệu suất cao
3. Hỗ trợ kiểm tra hiệu suất và benchmark
4. Triển khai các mẫu microservice có khả năng phục hồi

## Mục tiêu Dự án
- Thể hiện các nguyên tắc kiến trúc sạch thông qua thiết kế mô-đun
- Trình diễn triển khai DDD trong ngữ cảnh Spring Boot
- Cung cấp codebase có thể mở rộng và bảo trì
- Cho phép kiểm tra và tối ưu hiệu suất

## Công nghệ Sử dụng
- Java 21
- Spring Boot 3.3.5
- Maven quản lý dependencies
- Resilience4j cho khả năng chịu lỗi
- Logstash cho logging
- Docker cho container hóa

## Cấu trúc Mô-đun
1. nyano-start: Khởi tạo và cấu hình ứng dụng
2. nyano-controller: REST API endpoints và controllers
3. nyano-application: Dịch vụ ứng dụng và use cases
4. nyano-domain: Mô hình domain và logic nghiệp vụ
5. nyano-infrastructure: Tích hợp bên ngoài và các vấn đề hạ tầng

## Tính năng Chính
- REST API endpoints cho quản lý ticket
- Khả năng benchmark hiệu suất sử dụng Vegeta
- Thiết kế phục hồi với circuit breakers và khả năng chịu lỗi
- Logging có cấu trúc với Logstash
- Hỗ trợ triển khai container hóa 