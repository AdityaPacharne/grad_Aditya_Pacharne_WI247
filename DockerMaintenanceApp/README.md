# Docker Java + PostgreSQL Container Interaction

## ------------------------------------
## Task
## ------------------------------------

Create an application that:

- Create an application which can insert a record into a table(wrapped inside a container)
- The database need to be in other container (use any db, preferably postgres)
- Just make these 2 containers to interact with each other
- Also the compilation should be done in the same container

---

# Architecture

Java App Container  →  PostgreSQL Container  
(Both connected via Docker network)

---

# Step 1: Create Docker Network

```bash
docker network create task
```

# Step 2: Pull PostgreSQL Image

```bash
docker pull postgres
```

# Step 3: Run PostgreSQL Container

```bash
docker run --name postgres-db \
  --network task \
  -e POSTGRES_PASSWORD=root \
  -d postgres
```

Verify network
<img width="1268" height="258" alt="Screenshot 2026-02-25 165303" src="https://github.com/user-attachments/assets/305bb0b8-f9d8-4af8-9585-53a41eedf289" />

Access PostgreSQL

```bash
docker exec -it postgres-db psql -U postgres -W
```
Password: root

# Step 4: Create Dockerfile

```bash
FROM eclipse-temurin:17-jdk
WORKDIR /app
RUN apt-get update && apt-get install -y curl
RUN curl -o postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.7.3.jar
COPY . .
RUN javac MaintenanceApp.java
CMD ["java", "-cp", ".:postgresql.jar", "MaintenanceApp"]
```

# Step 5: Build Java Application Image

```bash
docker build -t maintenance .
```

# Step 5: Run Java Container

```bash
docker run --network task -it maintenance
```
