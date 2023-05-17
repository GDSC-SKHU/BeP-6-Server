FROM openjdk:17-alpine
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=production", "-jar","/app.jar"]
