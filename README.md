# Broadcast
[![Build Status](https://travis-ci.com/wireapp/broadcast.svg?branch=master)](https://travis-ci.com/wireapp/broadcast)

This is broadcast bot for Wire.

## Public endpoints:
```
    POST    /bots                     # must be visible to Wire BE
    POST    /bots/{bot}/messages      # must be visible to Wire BE
    POST    /broadcast                # must be visible to Your Broadcast Console
    GET     /status                   # use this endpoint as liveness probe
    GET     /swagger                  # API documentation
```

## Database scripts:
```
CREATE DATABASE $DATABASE_NAME;

CREATE TABLE Bots (
    bot_id UUID PRIMARY KEY
);

CREATE TABLE sessions (
    id         varchar NOT NULL,
    sid        varchar NOT NULL,
    data       bytea NOT NULL,
    PRIMARY KEY (id, sid)
);

CREATE TABLE identities (
    id         varchar PRIMARY KEY,
    data       bytea NOT NULL
);

CREATE TABLE prekeys (
    id         varchar NOT NULL,
    kid        integer NOT NULL,
    data       bytea NOT NULL,
    PRIMARY KEY (id, kid)
);

CREATE TABLE states (
    botId UUID NOT NULL PRIMARY KEY,
    bot json NOT NULL
);
```

## Build project
`mvn package`

## Run command
`java server broadcast.yaml`

## Environment variables:
```
APP_SERVICE_TOKEN    # obtained from Wire
APP_SECRET           # your application secret - some 24 random alpha numeric chars
POSTGRES_URL         # Postgres URL. format: jdbc:postgresql://<HOST>:<PORT>/<DB_NAME>  
POSTGRES_USER        # Postgres user
POSTGRES_PASSWORD    # Postgres user's password
```

## Build docker image from source code
docker build -t $DOCKER_USERNAME/broadcast:latest .

## Example of Docker run command
```
docker run -e APP_SERVICE_TOKEN='foo' \
-e APP_SECRET='bar' \
-e POSTGRES_URL='jdbc:postgresql://docker.for.mac.localhost/broadcast' \
-e POSTGRES_USER='postgres' \
-p 8080:80 $DOCKER_USERNAME/broadcast:latest
```

## How to broadcast a message in Wire
```
curl 'localhost:8080/broadcast' \
    -H "Authorization:Bearer $APP_SECRET" \
    -H "Content-Type:Application/JSON" \
    -d '{ "message" : "This is just a test" }'
```
