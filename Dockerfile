FROM maven:3.8.3-openjdk-17 AS builder

LABEL authors="Japa2k"

COPY settings.xml /root/.m2/settings.xml

WORKDIR /app
COPY . .
RUN mvn clean package -f pom.xml -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /
COPY --from=builder /app/target/products-0.0.1-SNAPSHOT.jar /app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN sed -i 's/\r$//' /wait-for-it.sh && chmod +x /wait-for-it.sh

ENTRYPOINT ["/wait-for-it.sh", "db", "5432", "--", "java", "-jar", "/app.jar"]