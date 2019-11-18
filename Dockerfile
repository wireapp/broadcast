FROM dejankovacevic/bots.runtime:2.10.3

COPY target/fraktionsruf.jar   /opt/fraktionsruf/fraktionsruf.jar
COPY fraktionsruf.yaml         /etc/fraktionsruf/fraktionsruf.yaml

WORKDIR /opt/fraktionsruf

EXPOSE  8080

CMD ["sh", "-c","/usr/bin/java -Djava.library.path=/opt/wire/lib -jar fraktionsruf.jar server /etc/fraktionsruf/fraktionsruf.yaml"]
