FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar ewm-stats/stats-service.jar
ENTRYPOINT ["java", "-jar", "ewm-stats/stats-service.jar"]