FROM openjdk:11
COPY target/recipe-suggestor*.jar /usr/src/recipe-suggestor.jar
COPY src/main/resources/application.properties /opt/conf/application.properties
CMD ["java", "-jar", "/usr/src/recipe-suggestor.jar", "--spring.config.location=file:/opt/conf/application.properties"]

