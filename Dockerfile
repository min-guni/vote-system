FROM openjdk:17
ARG JAVA_FILE=build/libs/*.jar
COPY ${JAVA_FILE}  app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]