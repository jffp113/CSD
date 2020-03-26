#!/usr/bin/env bash

SERVER_PORT=8443
REPLICA_ID=0
END_IP=2
EXTERNAL_BASE_PORT=8000

docker network create \
  --driver=bridge \
  --subnet=172.1.0.0/16 \
  --gateway=172.1.0.1 csd || true


for i in {1..4}
do
  docker run --rm -d --network=csd -p $((SERVER_PORT + i)):${SERVER_PORT} --ip "172.1.0.${END_IP}" -e "REPLICA_ID=${REPLICA_ID}"  -e "SERVER_PORT=${SERVER_PORT}" --name "replica${i}" server
  ((REPLICA_ID++))
  ((END_IP++))
done