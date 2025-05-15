FROM openjdk:17-jdk-slim
COPY target/currency-exchange-service.jar currency-exchange-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "currency-exchange-service.jar"]