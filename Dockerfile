FROM maven:3.9.9-eclipse-temurin-23 AS builder

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn dependency:go-offline

FROM maven:3.9.9-eclipse-temurin-23 AS setup

WORKDIR /app

COPY --from=builder /app /app

COPY /app.sh /app/app.sh
RUN chmod +x /app/app.sh

RUN mvn clean compile

ENTRYPOINT ["/bin/bash"]
