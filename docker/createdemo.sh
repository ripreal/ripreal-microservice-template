#!/bin/sh
cd ..\\docker
mvn package
cd ..
cd docker
docker-machine create --virtualbox-memory "4096" --driver virtualbox default
docker-machine start default
docker-machine regenerate-certs default
eval "$(docker-machine env default)"
docker-compose build
