FROM docker.io/maven AS build-env
WORKDIR /app
COPY pom.xml ./
RUN mvn verify --fail-never
COPY . ./
RUN mvn -Dmaven.test.skip=true package

FROM dejankovacevic/bots.runtime:2.10.3

#COPY --from=build-env /app/target/broadcast.jar /opt/broadcast/
COPY target/broadcast.jar         /opt/broadcast/
COPY broadcast.yaml               /opt/broadcast/

WORKDIR /opt/broadcast

EXPOSE  8080

CMD ["sh", "-c", "/usr/bin/java -jar broadcast.jar server broadcast.yaml"]
