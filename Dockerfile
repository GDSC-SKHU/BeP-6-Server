FROM openjdk:17-alpine
COPY build/libs/BeP-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=production", "-jar","/app.jar"]
