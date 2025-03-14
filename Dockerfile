FROM amazoncorretto:17

WORKDIR /app

COPY target/ImageStorage-0.0.1-SNAPSHOT.jar image-storage.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "image-storage.jar"]