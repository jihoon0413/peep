FROM openjdk:21-jdk

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]