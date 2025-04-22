# cdqtask — Getting Started & API Guide

Welcome to **DemoApp**, a Spring Boot service for managing `Person` entities and background classification `Task`. This guide will help you set up, run, and test the application locally using both Gradle and Docker.

## Table of Contents

1. [Prerequisites](#prerequisites)  
2. [Clone the Repository](#clone-the-repository)  
3. [Java & Environment Setup](#java--environment-setup)  
4. [Build with Gradle](#build-with-gradle)  
5. [Run Locally](#run-locally)  
6. [Run Tests](#run-tests)
7. [Run with Docker Compose](#run-with-docker-compose)  
8. [API Usage](#api-usage)  

---

## Prerequisites

- **Java 17+**  
- **Gradle 8.x** (wrapper included)  
- **Docker Desktop** (with WSL2 & virtualization enabled on Windows)  
- **Git**  
- (Optional) IDE: IntelliJ IDEA, VS Code, etc.  
- (Optional) Postman or Insomnia for testing

---

## Clone the Repository

```bash
git clone git@github.com:<your-username>/cdqtask.git
cd cdqtask
```

## Java & Environment Setup
Install Java 17+

Verify your version:

```bash
java -version
```
You should see something like:
```bash
openjdk version "17.0.8"
```

Set `JAVA_HOME` if needed

Windows:

Change the `PATH` if needed
```bash
setx JAVA_HOME "C:\Program Files\java\jdk-17"
```

macOS/Linux:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v17)
```

Force Gradle to use the right Java version
Edit gradle.properties and add:

```bash
org.gradle.java.home=C:/Program Files/java/jdk-17
```

## Build with Gradle
You can build the project using the included Gradle wrapper:

On macOS/Linux:

```bash
./gradlew clean build
```

On Windows:
```bash
gradlew.bat clean build
```

## Run with Docker Compose
Make sure Docker Desktop is running, then:

```bash
docker-compose up --build -d
```
This starts the redis and MySQL container.

App runs at: http://localhost:8081

MySQL exposed on port 3307

To stop and clean up:

```bash
docker-compose down
```

## Run Locally
Option 1: Run via Gradle

```bash
./gradlew bootRun
```

Or use IDE run configuration with default spring boot setup

Once running, the app will be available at:
http://localhost:8081


# Run Tests
To run unit tests:

```bash
./gradlew test
```

## API Usage

After running the app (see Getting Started), you can interact with the API using tools like Postman or Curl. Here's how it works:

1. **Create a Person**
   - Endpoint: `POST /api/person`
   - Request Body:
     ```json
     {
       "name": "Mike",
       "surname": "Rogalski",
       "birthDate": "2024-01-12"
     }
     ```
   - Response includes a `taskId` which starts a background classification task.

2. **Update a Person**
   - Endpoint: `PATCH /api/person/{id}`
   - Request Body:
     ```json
     {
       "name": "Tomasz",
       "surname": "Smith",
       "birthDate": "2024-01-12",
       "company": "thr"
     }
     ```
   - Also returns a new `taskId` and starts a task.

3. **Track Task Progress**
   - Use the `taskId` from the create/update response.
   - Endpoint: `GET /api/task/{taskId}`
   - Response shows current status, progress percentage, and field-level classification results like:
     ```json
     {
       "status": "COMPLETED",
       "taskProgress": "100%",
       "result": {
         "name": "HIGH",
         "surname": "HIGH",
         "birthDate": "HIGH",
         "company": "HIGH"
       }
     }
     ```

4. **List All Persons**
   - `GET /api/person/getAll` returns all stored persons with self and update links.

5. **List All Tasks**
   - `GET /api/task/getAll` returns all classification tasks.

- Tasks compare current vs. previous person data and classify field similarity (`HIGH`, `LOW`, `ADDED`).
- All endpoints support HAL-style `_links` for easy navigation.
- Caching is enabled via Redis — tweak settings in `application.properties` to suit your environment.
- For more details check openapi definition
