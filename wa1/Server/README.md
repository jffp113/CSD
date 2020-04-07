#Requisites

-Docker
-Maven
-Java 11+ (For compilation, not tested for Java 8 to 10)

#Compile project
Compile and creation of container:

mvn clean package --settings settings.xml -DskipTests

Create of database container

cd ./Database
./entrypoint.sh

#Run Project

./run.sh

#Stop Everything

./stop.sh


