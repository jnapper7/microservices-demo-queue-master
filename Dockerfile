FROM eclipse-temurin:11
WORKDIR /usr/src/app
COPY ./target/*.jar application.jar

ARG COMMIT
ARG BUILD_VERSION
ARG BUILD_DATE

EXPOSE 80
ENTRYPOINT ["java", "-jar", "application.jar"]