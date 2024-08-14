FROM openjdk:17
WORKDIR /app
COPY backend/target/backend*.jar /app/backend.jar
COPY backend/src/main/resources/application.properties /opt/conf/application.properties
COPY backend/src/main/resources/static /app/static
CMD ["java", "-jar", "/app/backend.jar", "--spring.config.location=file:/opt/conf/application.properties"]
