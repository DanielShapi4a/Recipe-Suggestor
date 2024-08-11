FROM openjdk:17
WORKDIR /app
COPY target/backend*.jar /app/backend.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/app/backend.jar", "--spring.config.location=file:/opt/conf/application.properties"]
