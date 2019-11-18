# Fraktionsruf
[![Build Status](https://travis-ci.com/wireapp/fraktionsruf.svg?branch=master)](https://travis-ci.com/wireapp/fraktionsruf)

This is broadcast bot for Wire.

## Public endpoints:
```
    POST    /fraktionsruf/bots                     # must be visible to Wire BE
    POST    /fraktionsruf/bots/{bot}/messages      # must be visible to Wire BE
    POST    /fraktionsruf/broadcast                # must be visible to Fraktionsruf Console
    GET     /fraktionsruf/status                   # use this endpoint as liveness probe
    GET     /fraktionsruf/bots/status
    GET     /fraktionsruf/swagger
    GET     /fraktionsruf/swagger.{type:json|yaml}
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
`java -Djava.library.path=$LD_LIBRARY_PATH server fraktionsruf.yaml`

## Environment variables:
```
LD_LIBRARY_PATH      /opt/wire/lib
APP_SERVICE_TOKEN    # obtained from Wire
APP_SECRET           # your application secret - some 24 random alpha numeric chars
POSTGRES_HOST
POSTGRES_URL
POSTGRES_PASSWORD
DATABASE_NAME
```

## Example of Docker run command
```
sudo docker run -e APP_SERVICE_TOKEN='foo' \
-e APP_SECRET='bar' \
-e POSTGRES_HOST='elephant-postgresql.prod.svc.cluster.local' \
-e POSTGRES_URL='jdbc:postgresql://elephant-postgresql.prod.svc.cluster.local:5432/fraktionsruf' \
-e POSTGRES_PASSWORD='foobar' \
-e DATABASE_NAME='fraktionsruf'
-p 8080:80 \
--name fraktionsruf_container dejankovacevic/fraktionsruf:latest
```

## How to broadcast a message in Wire
```
curl 'localhost:8080/fraktionsruf/broadcast' \
    -H "Authorization:Bearer $APP_SECRET" \
    -H "Content-Type:Application/JSON" \
    -d '{ "message" : "This is just a test" }'
```