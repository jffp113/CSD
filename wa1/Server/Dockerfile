FROM openjdk
COPY ./config/ ./config
COPY ./Server-files/ ./Server-files/
COPY ./target/csd-0.0.1-SNAPSHOT-exec.jar .
ENTRYPOINT ["java","-jar","csd-0.0.1-SNAPSHOT-exec.jar","-Djava.security.properties=\"./config/java.security\" -Dlogback.configurationFile=\"./config/logback.xml\""]