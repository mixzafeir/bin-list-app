FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY src /app/src
COPY gradlew /app/
COPY gradle /app/gradle
RUN chmod +x ./gradlew
RUN ./gradlew build -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/etravli-0.0.1-SNAPSHOT.jar"]
