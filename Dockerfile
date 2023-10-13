FROM adoptopenjdk:17-jdk as builder
WORKDIR /shop
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew dependencies
COPY src src
RUN ./gradlew clean bootJar

FROM adoptopenjdk:17-jre
WORKDIR /shop
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0"
COPY --from=builder /app/build/libs/*.jar shop.jar
CMD ["java", "-jar", "shop.jar"]
