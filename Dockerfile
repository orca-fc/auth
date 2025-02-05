FROM openjdk:17

WORKDIR /app

COPY build/libs/*.jar /app/app.jar
COPY src/main/resources/ /app/resources/

CMD ["java", "-jar", "./app.jar", "--spring.config.location=file:./resources/"]