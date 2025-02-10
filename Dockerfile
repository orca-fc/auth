FROM openjdk:17

WORKDIR /app

ARG JWT_SECRET
ENV JWT_SECRET=${JWT_SECRET}

COPY build/libs/*.jar /app/app.jar
COPY src/main/resources/ /app/resources/

CMD ["java", "-jar", "./app.jar", "--spring.config.location=file:./resources/"]