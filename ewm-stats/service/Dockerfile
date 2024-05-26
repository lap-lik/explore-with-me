FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm-stats-service.jar
ENTRYPOINT ["java", "-jar", "ewm-stats-service.jar"]