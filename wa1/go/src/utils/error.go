package utils

import (
	"SGX_CSD_Server/src/server/repository"
	"encoding/json"
	"log"
	"strconv"
)

type Message struct {
	msg string
}

type JsonConvertable interface {
	ToJSON() []byte
}

func (m Message) ToJSON() []byte {
	return TypeToJsonByte(m)
}

var invalidMessage = append(append(make([]byte, 0, 0), byte(1)), []byte("Invalid Parameters")...)

func CreateInvalidMessage() []byte {
	return invalidMessage
}

func InternalError(e error) []byte {
	return append(append(make([]byte, 0, 0), byte(1)), []byte(e.Error())...)
}

var invalidAccount = append(append(make([]byte, 0, 0), byte(1)), []byte("Account Does not exist or does not have enough money")...)

func AccountDoesNotExistOrDoesNotHaveEnoughMoneyMessage() []byte {
	return invalidAccount
}

func CreateMessage(js JsonConvertable) []byte {
	bs := make([]byte, 0, 0)

	bs = append(bs, byte(0))
	bs = append(bs, js.ToJSON()...)

	return bs
}

func CreateMessageByTransactions(js []repository.Transaction) []byte {
	bs := make([]byte, 0, 0)

	bs = append(bs, byte(0))
	json, err := json.Marshal(js)

	if err != nil {
		log.Fatal(err)
	}

	bs = append(bs, json...)

	return bs
}

func CreateMessageBaseType(i int) []byte {
	bs := make([]byte, 0, 0)
	bs = append(bs, byte(0))
	bs = append(bs, []byte(strconv.Itoa(i))...)

	return bs
}

var auctionDoesNotExist = append(append(make([]byte, 0, 0), byte(1)), []byte("Account Does not exist or does not have enough money")...)

func AuctionDoesNotExistMessage() []byte {
	return auctionDoesNotExist
}

var noBidsInAuction = append(append(make([]byte, 0, 0), byte(1)), []byte("No Bids in this Auction")...)

func NoBidsInAuction() []byte {
	return noBidsInAuction
}

var bidInvalid = append(append(make([]byte, 0, 0), byte(1)), []byte("Bid Invalid")...)

func BidInvalidMessage() []byte {
	return bidInvalid
}

var notEnoughMoney = append(append(make([]byte, 0, 0), byte(1)), []byte("User Does Not have enough Money")...)

func NotEnoughMoney() []byte {
	return notEnoughMoney
}

var auctionIsClosed = append(append(make([]byte, 0, 0), byte(1)), []byte("Auction Is Closed")...)

func AuctionIsClosed() []byte {
	return notEnoughMoney
}

func CreateMessageByAuction(js []repository.Auction) []byte {
	bs := make([]byte, 0, 0)

	bs = append(bs, byte(0))
	j, err := json.Marshal(js)

	if err != nil {
		log.Fatal(err)
	}

	bs = append(bs, j...)

	return bs
}

func CreateMessageByBids(js []repository.Bid) []byte {
	bs := make([]byte, 0, 0)

	bs = append(bs, byte(0))
	j, err := json.Marshal(js)

	if err != nil {
		log.Fatal(err)
	}

	bs = append(bs, j...)

	return bs
}

func CreateMessageByBid(js *repository.Bid) []byte {
	bs := make([]byte, 0, 0)

	bs = append(bs, byte(0))
	j, err := json.Marshal(js)

	if err != nil {
		log.Fatal(err)
	}

	bs = append(bs, j...)

	return bs
}
