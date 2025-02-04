FROM openjdk:17-alpine
LABEL maintainer="Orynbekdanial8@gmail.com"
RUN apk update && apk add curl
COPY target/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]