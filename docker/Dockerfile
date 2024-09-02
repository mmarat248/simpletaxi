FROM gradle:8-jdk-21-and-22-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM amazoncorretto:22-alpine-jdk
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
