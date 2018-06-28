FROM openjdk:8-jre-alpine

LABEL maintainer="name@host.com"

# опции jvm и приложения
ENV JAVA_OPTS="-Dspring.profiles.active=test" APP_OPTS="" APP_PORT=8080

VOLUME /tmp

RUN mkdir -p /var/www/app
COPY target/user-service.jar /var/www/app/application.jar
WORKDIR /var/www/app

EXPOSE ${APP_PORT}

CMD ["sh", "-c", "java ${JAVA_OPTS} ${APP_OPTS} -jar application.jar"]
