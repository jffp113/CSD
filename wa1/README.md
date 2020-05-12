# Final
The name "wa1" does not correspond to the version of the project.

Project Version **WA4**

# Change List

- 9/05/2020 ->  Reduce Container size from 541 MB to 256 MB to fit better in sgx
- 12/05/2020 -> Change database from MySQL to Redis


# WA4 What was implemented

- Smart Contracts
- Async signed Replies sent to client and verified by them.

# Requisites

- Docker
- Maven
- Java 11+ (For compilation, not tested for Java 8 to 10)

# Compile and Run project Server

There is a script that allows you to compile and build the project. You can concatenate commands:

Lets see:

With this command you build the project

```
./run.sh upV 3 1
```

If you want to start 3 correct replicas and 1 byzantine (On walletController)

```
./run.sh build
```

If you want to build and execute the servers you do:

```
./run.sh build up
```

If you want to stop the servers you do

```
./run.sh down
```

If you want to stop clean the databases and start the server you do:

```
./run.sh down clear up
```


You can concatenate any command shown here:
```
Usage: 
  run.sh <Mode>
    <Mode>
      - 'up' - bring up the service
      - 'upV <correct> <byzantine>' - bring up the service with specified correct and byzantine replicas
      - 'down' - bring down the service
      - 'clear' - clear database, take effect on next startup
      - 'restart' - restart the service
      - 'build' - container image creation for project and database
```




