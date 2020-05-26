#FROM adoptopenjdk/openjdk12:jre-12.0.2_10-alpine
FROM sconecuratedimages/apps:openjdk-11-alpine-scone3.0
COPY ./config/ ./config
COPY ./libs/ ./libs
COPY ./Server-files/ ./Server-files/
COPY ./entry.sh .
RUN chmod +x ./entry.sh
COPY ./target/csd-0.0.1-SNAPSHOT-exec.jar .
#ENTRYPOINT ["java","-cp","\"csd-0.0.1-SNAPSHOT-exec.jar:libs/*\"","org.springframework.boot.loader.JarLauncher","-Djava.security.properties=\"./config/java.security\" -Dlogback.configurationFile=\"./config/logback.xml\""]
ENTRYPOINT ["./entry.sh"]