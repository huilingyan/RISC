#!/bin/bash

# first terminate any old ones
docker kill citest-651
docker rm citest-651

# now run the new one
# set up postgres
sudo apt-get install -y postgresql postgresql-client libpq-dev
sudo -u postgres psql -d template1
template1=# CREATE USER runner WITH PASSWORD 'abc123' CREATEDB;
template1=# CREATE DATABASE risc OWNER runner;
template1=# \q
psql -U runner -h localhost -d risc -W

# old code
docker run -d --name citest-651 -p 1651:7777 6666:6666 -t citest ./gradlew run-server


# docker compose
# sudo apt-get install -y docker-compose
# docker-compose up

