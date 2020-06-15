package server

import (
	"SGX_CSD_Server/src/server/controller"
	"bufio"
	"log"
	"net"
)

const (
	CreateAuction     = "CREATE_AUCTION"
	CreateBidAuction  = "CREATE_BID_AUCTION"
	TerminateAuction  = "TERMINATE_AUCTION"
	GetCloseBid       = "GET_CLOSE_BID"
	GetOpenAuctions   = "GET_OPEN_AUCTIONS"
	GetClosedAuctions = "GET_CLOSED_AUCTIONS"
	GetAuctionBids    = "GET_AUCTION_BIDS"
	GetClientBids     = "GET_CLIENT_BIDS"
)

const (
	CreateMoney     = "CREATE_MONEY"
	TransferMoney   = "TRANSFER_MONEY"
	GetMoney        = "GET_MONEY"
	GetLedger       = "GET_LEDGER"
	GetClientLedger = "GET_CLIENT_LEDGER"
)

const (
	CreateSmart = "CREATE_SMART"
	RemoveSmart = "REMOVE_SMART"
	ListSmart   = "LIST_SMART"
	GetSmart    = "GET_SMART"
)

func HandleEncryptedRequest(connection net.Conn) []byte {
	log.Println("Decript Request", connection.RemoteAddr())
	reader := bufio.NewReader(connection)
	//Decript
	//Path \n
	//Json -> repectivo method
	//
	//
	//MAC
	return deliver(reader)
}

func deliver(reader *bufio.Reader) []byte {
	bytes, err := reader.ReadBytes('\n')
	checkErrorIfNotNullPanic(err)
	path := string(bytes[:len(bytes)-1])

	log.Println("Operation Path ", path)

	switch path {
	case CreateAuction:
		return controller.CreateAuction(reader)
	case CreateBidAuction:
		return controller.MakeBid(reader)
	case TerminateAuction:
		return controller.TerminateAuction(reader)
	case GetCloseBid:
		return controller.GetCloseBid(reader)
	case GetOpenAuctions:
		return controller.GetOpenAuctions(reader)
	case GetClosedAuctions:
		return controller.GetClosedAuction(reader)
	case GetAuctionBids:
		return controller.GetAuctionBids(reader)
	case GetClientBids:
		return controller.GetClientBids(reader)

	case CreateMoney:
		return controller.CreateMoney(reader)
	case TransferMoney:
		return controller.TransferMoneyBetweenUsers(reader)
	case GetMoney:
		return controller.CurrentAmount(reader)
	case GetLedger:
		return controller.LedgerOfClientTransfers(reader)
	case GetClientLedger:
		return controller.LedgerOfClientTransfersById(reader)
	}
	return []byte{}
}

func checkErrorIfNotNullPanic(err error) {
	if err != nil {
		panic(err)
	}
}
