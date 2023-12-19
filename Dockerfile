#base image
FROM openjdk:19-alpine

#author name
MAINTAINER shubham

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} hardware.jar
ENTRYPOINT ["java","-jar","/hardware.jar"]

#WORKDIR usr/src/bootapp
#COPY target/ShubhamHardware_Backend-0.0.1-SNAPSHOT.jar app.jar
#CMD ["java","-jar","app.jar"]