FROM openjdk:17-alpine
EXPOSE 8082
COPY target/user-mgmt-0.0.1-SNAPSHOT.jar user-mgmt.jar
ENTRYPOINT ["java","-jar","user-mgmt.jar"]
