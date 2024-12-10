FROM openjdk:21-jdk

WORKDIR /u/myapp
# COPY build/libs/*[^plain].jar ./
# CMD java -Dserver.port=8080 -Dspring.profiles.active=production -jar *.jar


CMD ["./gradlew", "clean", "build"]

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]