FROM openjdk:17-jdk as builder
WORKDIR /shop
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN microdnf install findutils
RUN chmod +x ./gradlew
RUN ./gradlew dependencies
COPY src src
RUN ./gradlew clean build

FROM openjdk:17
WORKDIR /shop
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0"
COPY --from=builder shop/build/libs/tg_bot-0.0.1-SNAPSHOT.jar shop.jar
CMD ["java", "-jar", "shop.jar"]