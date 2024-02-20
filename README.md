# Broadcast
This is broadcast bot for Wire.

## API Documentation
https://services.wire.com/fdp/swagger#/default

## Exposed endpoints on 8080 port:
```
    POST    /bots                     # must be visible to Wire BE
    POST    /bots/{bot}/messages      # must be visible to Wire BE
    POST    /broadcast                # must be visible to Your Broadcast Console
    POST    /confluence               # must be visible to Script Runner
    GET     /healthcheck              # use this endpoint as liveness probe
    GET     /swagger                  # API documentation
```

## Database scripts:
```
CREATE DATABASE $DATABASE_NAME;
```

## Build project
`mvn package`

## Run command
`java -jar broadcast.jar server broadcast.yaml`

## Environment variables:
```
FRAKTIONRUF_TOKEN   # Obtained from Wire
CONFLUENCE_TOKEN    # Obtained from Wire
DB_URL         # Postgres URL. format: jdbc:postgresql://<HOST>:<PORT>/<DB_NAME>  
DB_USER        # Postgres user
DB_PASSWORD    # Postgres user's password  
```

## Build docker image from source code
docker build -t $DOCKER_USERNAME/fraktionsruf:0.1.1 .

## Example of Docker run command
```
docker run \ 
-e FRAKTIONRUF_TOKEN='foo' \  
-e CONFLUENCE_TOKEN='bar' \  
-e DB_URL='jdbc:postgresql://docker.for.mac.localhost/broadcast' \
-e DB_USER='postgres' \ 
-e DB_PASSWORD='secret' \
-p 80:8080 \
--name broadcast --rm $DOCKER_USERNAME/fraktionsruf:0.1.1
```

## How to broadcast a message in Wire using curl
```
curl 'localhost:8080/fdp/broadcast' \
    -H "Authorization:Bearer $FRAKTIONRUF_TOKEN" \
    -H "Content-Type:Application/JSON" \
    -d '{ "message" : "Hello from Fra*tionruf" }'
```

## Invoke Confluence webhook
```
curl 'localhost:8080/fdp/confluence' \
    -H "Authorization:Bearer $CONFLUENCE_TOKEN" \
    -H "Content-Type:Application/JSON" \
    -d '{ "message" : "Hello from Confluence" }'
```