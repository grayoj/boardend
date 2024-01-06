FROM openjdk:21-jdk

WORKDIR /app

COPY target/boardend-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "boardend-0.0.1-SNAPSHOT.jar"]
