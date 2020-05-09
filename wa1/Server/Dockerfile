FROM openjdk:12
COPY ./config/ ./config
COPY ./libs/ ./libs
COPY ./Server-files/ ./Server-files/
COPY ./target/csd-0.0.1-SNAPSHOT-exec.jar .
COPY ./entry.sh .
RUN chmod +x ./entry.sh
#ENTRYPOINT ["java","-cp","\"csd-0.0.1-SNAPSHOT-exec.jar:libs/*\"","org.springframework.boot.loader.JarLauncher","-Djava.security.properties=\"./config/java.security\" -Dlogback.configurationFile=\"./config/logback.xml\""]
ENTRYPOINT ["./entry.sh"]