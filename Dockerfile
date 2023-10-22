FROM openjdk:11-jdk
LABEL maintainer="dev-blog"
ARG JAR_FILE=build/libs/dev-blog-0.0.1-SNAPSHOT.jar
#ADD ${JAR_FILE} docker-springboot.jar
COPY ${JAR_FILE} app.jar
COPY application-prod.yml application-peos.yml
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/docker-springboot.jar"]
CMD ["java","-jar","-Dspring.profiles.active=prod","app.jar"]