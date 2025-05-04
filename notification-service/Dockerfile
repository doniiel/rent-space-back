FROM openjdk:17-alpine
LABEL maintainer="Orynbekdanial8@gmail.com"
RUN apk update && apk add curl
COPY target/app.jar app.jar
USER 1001
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]