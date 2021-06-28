FROM openjdk:8-jdk-alpine
MAINTAINER jason_lim
COPY target/hr-0.0.1-SNAPSHOT.jar hr-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/hr-0.0.1-SNAPSHOT.jar"]