FROM eclipse-temurin:17-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y curl
RUN curl -o postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.7.3.jar
COPY . .
RUN javac MaintenanceApp.java
CMD ["java", "-cp", ".:postgresql.jar", "MaintenanceApp"]
