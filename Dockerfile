FROM gradle:8.10.1-jdk21-jammy AS build

ARG GITHUB_ACTOR
ARG GITHUB_TOKEN
ENV GITHUB_ACTOR=$GITHUB_USER
ENV GITHUB_TOKEN=$GITHUB_TOKEN

COPY . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble

FROM openjdk:21-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
COPY newrelic-agent/newrelic.jar /app/newrelic.jar
COPY ./newrelic-agent/newrelic.yml /app/newrelic.yml

WORKDIR /app
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "-javaagent:/app/newrelic.jar", "/app/spring-boot-application.jar"]


