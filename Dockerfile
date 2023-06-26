FROM openjdk:19-alpine

COPY target/*.jar restapi.jar

ENTRYPOINT ["java", "-jar", "/restapi.jar"]

EXPOSE 8080