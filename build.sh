#!/usr/bin/env bash
#docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD
#mvn package -DskipTests=true -Dmaven.javadoc.skip=true
docker build -t $DOCKER_USERNAME/fraktionsruf:0.1.2 .
docker push $DOCKER_USERNAME/fraktionsruf
kubectl delete pod -l name=fdp -n prod
kubectl get pods -l name=fdp -n prod
