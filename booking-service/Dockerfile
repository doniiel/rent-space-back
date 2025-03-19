FROM openjdk:17-alpine
LABEL maintainer="Orynbekdanial8@gmail.com"
WORKDIR /app
RUN apk update && apk add curl
COPY target/app.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]