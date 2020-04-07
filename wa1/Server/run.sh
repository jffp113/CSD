#!/usr/bin/env bash

SERVER_PORT=8443
REPLICA_ID=0
END_IP=2
EXTERNAL_BASE_PORT=8000


function printHelp() {
  echo "Usage: "
  echo "  run.sh <Mode>"
  echo "    <Mode>"
  echo "      - 'up' - bring up the service"
  echo "      - 'upB' - compiling and deploying service"
  echo "      - 'down' - bring down the service"
  echo "      - 'downC' - bring down the service and clear databases"
  echo "      - 'restart' - restart the service"
  echo "      - 'restartC' - clean restart the service"
  echo "      - 'build' - compiling and container creation for project and database"
}

function startService() {

  docker network create \
  --driver=bridge \
  --subnet=172.1.0.0/16 \
  --gateway=172.1.0.1 csd || true

    echo "Service Containers Starting"

    for i in {1..4}
    do
      rm -rf "$(pwd)/Database/db${i}"
      docker run --rm -d --network=csd -v "$(pwd)/Database/db${i}":/var/lib/mysql -e MYSQL_ROOT_PASSWORD=toor --name "db${i}" csddatabase
    done

    sleep 15

    for i in {1..4}
    do
      docker run --rm -d --network=csd -p $((SERVER_PORT + i)):${SERVER_PORT}  -e MYSQL_HOST="db${i}" \
       -e "REPLICA_ID=${REPLICA_ID}"  -e "SERVER_PORT=${SERVER_PORT}" --name "replica${i}" server
      ((REPLICA_ID++))
      ((END_IP++))
    done

    echo "Service Containers Started"
}

function stopService() {
    echo "Service Containers Stopping"
    docker container stop replica1 replica2 replica3 replica4 db1 db2 db3 db4
    echo "Service Containers Stopped"
}

function stopServiceAndClearDatabase() {
    stopService
    rm -rf ./Database/db*
}

function buildService() {
  mvn clean package --settings settings.xml -DskipTests
  cd Database
  docker build -t csddatabase .
  cd ..
}

if (! docker stats --no-stream); then
  echo "Docker Is Not Running"
  exit 0;
fi

## Parse mode
if [[ $# -lt 1 ]]; then
  printHelp
  exit 0
else
  MODE=$1
  shift
fi

if [ "$MODE" == "up" ]; then
  echo "Starting Service"
  echo
  startService
elif [ "$MODE" == "down" ]; then
  stopService
elif [ "$MODE" == "build" ]; then
  buildService
elif [ "$MODE" == "downC" ]; then
  stopServiceAndClearDatabase
elif [ "$MODE" == "upB" ]; then
  buildService
  startService
elif [ "$MODE" == "restart" ]; then
  stopService
  startService
elif [ "$MODE" == "restartC" ]; then
  stopServiceAndClearDatabase
  startService
else
  echo "Command $MODE does not exist"
fi