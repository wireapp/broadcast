# Fraktionsruf
[![Build Status](https://travis-ci.org/wireapp/fraktionsruf.svg?branch=master)](https://travis-ci.org/wireapp/fraktionsruf)

This is broadcast bot for Wire.

## Public endpoints:
```
    POST    /fraktionsruf/bots
    GET     /fraktionsruf/bots/status
    POST    /fraktionsruf/bots/{bot}/messages
    POST    /fraktionsruf/broadcast
    GET     /fraktionsruf/status
    GET     /fraktionsruf/swagger
    GET     /fraktionsruf/swagger.{type:json|yaml}
```

## Environment variables:
```
LD_LIBRARY_PATH
APP_SERVICE_TOKEN
APP_SECRET
POSTGRES_HOST
POSTGRES_URL
POSTGRES_PASSWORD
DATABASE_NAME
```

## Database scripts:
```
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

## How to broadcast a message in Wire
```
curl 'localhost:8080/fraktionsruf/broadcast' \
    -H "Authorization:Bearer $APP_SECRET" \
    -H "Content-Type:Application/JSON" \
    -d '{ "message" : "This is just a test" }'
```