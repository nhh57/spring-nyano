#mvn clean install -DskipTests; docker rm -f spring-nyano; docker rmi -f spring-nyano:v1.0.0;docker build --no-cache -t spring-nyano:v1.0.0 .;docker run -d -p 1122:1122 --name spring-nyano spring-nyano:v1.0.0

FROM amazoncorretto:21-alpine AS build
RUN apk add --no-cache binutils
WORKDIR /opt

RUN $JAVA_HOME/bin/jlink \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /opt/jdk

# Stage 2: Chạy ứng dụng với JDK tối ưu
FROM alpine:latest

# Sao chép JDK tối ưu vào container
COPY --from=build /opt/jdk /opt/jdk

# Thiết lập JAVA_HOME và PATH
ENV JAVA_HOME=/opt/jdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Sao chép file JAR và cấu hình vào container
COPY ./nyano-start/target/nyano-start-0.0.1-SNAPSHOT.jar /app.jar
# Expose port
EXPOSE 1122

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]