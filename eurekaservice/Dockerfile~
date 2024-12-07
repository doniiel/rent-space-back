FROM openjdk:17-alpine
LABEL maintainer="Orynbekdanial8@gmail.com"
RUN apk update && apk add curl
COPY target/configservice-0.0.1-SNAPSHOT.jar configservice-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "configservice-0.0.1-SNAPSHOT.jar"]