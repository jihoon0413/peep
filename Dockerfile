FROM openjdk:21-jdk

ENTRYPOINT ["java", "-jar", "/app/app.jar"]