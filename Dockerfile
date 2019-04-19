FROM java:8-alpine

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY target/app.jar /usr/src/app/

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
