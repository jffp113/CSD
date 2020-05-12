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
  echo "      - 'upV <correct> <byzantine>' - bring up the service with specified correct and byzantine replicas"
  echo "      - 'down' - bring down the service"
  echo "      - 'clear' - clear database, take effect on next startup"
  echo "      - 'restart' - restart the service"
  echo "      - 'build' - container image creation for project and database"
}

function startService() {

  docker network create \
  --driver=bridge \
  --subnet=172.1.0.0/16 \
  --gateway=172.1.0.1 csd || true
    echo "Service Containers Starting"
    echo "Service Database Starting"
    for (( i=1; i<=(($1 + $2)); i++ ))
    do
      #rm -rf "$(pwd)/Database/db${i}"
      docker run --rm -d --network=csd -v "$(pwd)/Database/db${i}":/data --name "db${i}" redis:alpine
    done
    echo "Service Database Started"

    echo "Waiting 1 minute before starting replicas"
    #sleep 60
    echo "Starting replicas"


    #No Byzantines
    #for i in {1..$(($1))}
    for (( i=1; i<=$1; i++ ))
    do
      echo "Starting correct replica $i"

      docker run --rm -d --network=csd -p $((SERVER_PORT + i)):${SERVER_PORT}  -e DB_HOST="db${i}" \
       -e "REPLICA_ID=${REPLICA_ID}"  -e "SERVER_PORT=${SERVER_PORT}" -e "REPLICA_BYZ=false" --name "replica${i}" server
      ((REPLICA_ID++))
      ((END_IP++))

    done

    #Byzantines
    for (( i=$1 + 1; i<=$2 + $1; i++ ))
    do
      echo "Starting correct byzantine $(($i - $1))"
    docker run --rm -d --network=csd -p $((SERVER_PORT + $2 + $i)):${SERVER_PORT}  -e MYSQL_HOST="db${i}" \
       -e "REPLICA_ID=${REPLICA_ID}"  -e "SERVER_PORT=${SERVER_PORT}" -e "REPLICA_BYZ=true" --name "replica${i}" server
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

function clearDatabase() {
    echo "Clearing Database"
    rm -rf ./Database/db*
    echo "Cleared Database"
}

function buildService() {
  mvn clean package --settings settings.xml -DskipTests
  #cd Database
  #docker build -t csddatabase .
  #cd ..
}

if (! docker stats --no-stream); then
  echo "Docker Is Not Running"
  exit 0;
fi


if [[ $# -lt 1 ]]; then
  printHelp
  exit 0
fi

while [[ $# -gt 0 ]]; do

## Parse mode
MODE=$1
shift

if [ "$MODE" == "up" ]; then
  echo "Starting Service"
  startService 4 0
elif [ "$MODE" == "upV" ]; then
  echo "Starting Service Correct=$1 Byzantines=$2"
  startService $1 $2
  shift
  shift
elif [ "$MODE" == "down" ]; then
  stopService
elif [ "$MODE" == "build" ]; then
  buildService

elif [ "$MODE" == "clear" ]; then
  clearDatabase
elif [ "$MODE" == "restart" ]; then
  stopService
  startService
else
  echo "Command $MODE does not exist"
fi

done