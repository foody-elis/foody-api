FROM gradle:jdk-21-and-22-alpine AS build

WORKDIR /home/gradle/src

COPY --chown=gradle:gradle .. .

RUN gradle build --no-daemon -x test

FROM openjdk:21-jdk

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

COPY /docker/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]