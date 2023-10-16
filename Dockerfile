FROM openjdk:17
WORKDIR /shop
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=90.0"
COPY .build/libs/tg_bot-0.0.1-SNAPSHOT.jar shop.jar
RUN echo '#!/bin/sh' > run.sh && \
    echo 'echo "Starting shop.jar..."' >> run.sh && \
    echo 'java -jar shop.jar' >> run.sh && \
    echo 'if [ $? -eq 0 ]; then' >> run.sh && \
    echo '    echo "shop.jar exited with success"' >> run.sh && \
    echo 'else' >> run.sh && \
    echo '    echo "shop.jar exited with error code: $?"' >> run.sh && \
    echo 'fi' >> run.sh && \
    chmod +x run.sh
CMD ["./run.sh"]
