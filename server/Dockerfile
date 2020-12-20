FROM openjdk:11-jre-slim
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/server-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]