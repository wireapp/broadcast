FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn verify --fail-never
COPY . ./
RUN mvn -Dmaven.test.skip=true package

FROM wirebot/runtime:1.4.0

COPY --from=build /app/target/broadcast.jar /opt/broadcast/
COPY broadcast.yaml               /opt/broadcast/

WORKDIR /opt/broadcast

EXPOSE  8080
EXPOSE  8081

ENTRYPOINT ["java", "-jar", "broadcast.jar", "server", "broadcast.yaml"]