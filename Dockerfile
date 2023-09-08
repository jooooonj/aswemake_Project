FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/project-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev,secret","/app.jar"]