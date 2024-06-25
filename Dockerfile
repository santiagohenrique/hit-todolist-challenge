FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=target/hit-todolist-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar
COPY src/main/resources/credentials.json /app/src/main/resources/credentials.json

EXPOSE 8080
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
