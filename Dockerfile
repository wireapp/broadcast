FROM dejankovacevic/bots.runtime:2.10.3

COPY target/broadcast.jar   /opt/fraktionsruf/
COPY broadcast.yaml         /etc/fraktionsruf/

WORKDIR /opt/broadcast

EXPOSE  8080

CMD ["sh", "-c","/usr/bin/java -jar broadcast.jar server /etc/broadcast/broadcast.yaml"]
