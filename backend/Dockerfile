FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

# Environment variables
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=prod

COPY --from=build /app/target/saas-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:${PORT:-8080}/api/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} $JAVA_OPTS -jar /app/app.jar"]
