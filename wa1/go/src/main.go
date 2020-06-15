package main

import (
	"SGX_CSD_Server/src/server"
	"SGX_CSD_Server/src/server/repository"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"os"
)

const (
	configFileName = "conf.json"
)

type Configuration struct {
	ServerPort string
}

func main() {
	config := loadConfig()
	fmt.Printf("server Initializating in port %s\n", config.ServerPort)

	log.Println("DB HOST:",os.Getenv("DB"))

	tcpListener, err := net.Listen("tcp4", config.ServerPort)
	checkErrorIfNotNullPanic(err)

	repository.Connect()
	for {
		connection, err := tcpListener.Accept()
		checkErrorIfNotNullPanic(err)

		go handleConnection(connection)
	}

}

func handleConnection(connection net.Conn) {
	defer connection.Close()

	connection.Write(
		server.HandleEncryptedRequest(connection))
}

func loadConfig() Configuration {
	configFile, err := os.Open(configFileName)

	checkErrorIfNotNullPanic(err)
	defer configFile.Close()

	decoder := json.NewDecoder(configFile)

	configuration := Configuration{}
	err = decoder.Decode(&configuration)

	checkErrorIfNotNullPanic(err)

	return configuration
}

func checkErrorIfNotNullPanic(err error) {
	if err != nil {
		panic(err)
	}
}
