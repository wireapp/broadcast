# Alert-bot
[![Build Status](https://travis-ci.org/wireapp/alert-bot.svg?branch=master)](https://travis-ci.org/wireapp/alert-bot)

This is alert bot for Wire.

## How to trigger a _Simple_ broadcast manually
```
curl 'localhost:8080/fdp/broadcast' \
    -H "Authorization:Bearer $SECRET" \
    -H "Content-Type:Application/JSON" \
    -d '{ "message" : "This is just a test" }'
```
