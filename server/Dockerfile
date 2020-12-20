FROM openjdk:11-jre-slim
VOLUME /tmp
EXPOSE 8080
ENV DB_URL="jdbc:postgresql://ec2-52-30-161-203.eu-west-1.compute.amazonaws.com:5432/dg5p42df8q995"
ENV DB_USERNAME="bnexuuveperybj"
ENV DB_PASSWORD="8439c9659914179deb76a4d4a1cde5fa57f5c9708541b97d99af3e5d7179fe9b"
ARG JAR_FILE=build/libs/server-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]