FROM openjdk:17
WORKDIR /shop
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0"
COPY shop.jar shop.jar
CMD ["java", "-jar", "shop.jar"]