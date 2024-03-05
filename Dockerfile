FROM openjdk:11-jdk
LABEL maintainer="dev-blog"

ARG JAR_FILE=./build/libs/dev-blog-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

COPY application-prod.yml application-prod.yml

CMD ["java","-jar","-Dspring.profiles.active=prod","app.jar"]