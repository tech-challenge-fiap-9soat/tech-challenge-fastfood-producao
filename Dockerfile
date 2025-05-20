FROM openjdk:21-jdk-slim-buster

WORKDIR /app

COPY ./target/fastfood-producao-*.jar application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/application.jar"]