#!/bin/sh

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -cp "csd-0.0.1-SNAPSHOT-exec.jar:libs/*" org.springframework.boot.loader.JarLauncher -Djava.security.properties="./config/java.security" -Dlogback.configurationFile="./config/logback.xml"