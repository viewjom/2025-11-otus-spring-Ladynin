FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
COPY ./target/hw17-docker.jar /app/docker.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app/docker.jar"]