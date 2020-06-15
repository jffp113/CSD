package controller

import (
	"SGX_CSD_Server/src/server/repository"
	"SGX_CSD_Server/src/utils"
	"bufio"
	"log"
)

type createAuctionParam struct {
	OwnerId string
}

func CreateAuction(r *bufio.Reader) []byte {
	var p createAuctionParam
	utils.ReaderJsonTo(r, &p)
	u := repository.AccountFindById(p.OwnerId)
	log.Println("Creating auction for ", p.OwnerId)

	if u == nil {
		return utils.AccountDoesNotExistOrDoesNotHaveEnoughMoneyMessage()
	}

	a := repository.Auction{
		OwnerId:   p.OwnerId,
		LastBidId: -1,
		IsClosed:  false,
	}

	i, _ := repository.InsertOneAuction(&a)

	return utils.CreateMessageBaseType(i)
}

type terminateAuctionParam struct {
	AuctionId int
}

func TerminateAuction(r *bufio.Reader) []byte {
	var p terminateAuctionParam
	utils.ReaderJsonTo(r, &p)

	res, err := repository.CloseOneAuction(p.AuctionId)

	if err != nil {
		return utils.InternalError(err)
	}

	if res <= 0 {
		return utils.AuctionDoesNotExistMessage()
	}

	return []byte{byte(0)}
}

func GetOpenAuctions(r *bufio.Reader) []byte {
	a := repository.GetOpenAuctionIs(false)
	return utils.CreateMessageByAuction(a)
}

func GetClosedAuction(r *bufio.Reader) []byte {
	a := repository.GetOpenAuctionIs(true)
	return utils.CreateMessageByAuction(a)
}

type getAuctionBidsParam struct {
	AuctionId int
}

func GetAuctionBids(r *bufio.Reader) []byte {
	var p getAuctionBidsParam
	utils.ReaderJsonTo(r, &p)

	b := repository.GetAllBidsByAuctionId(p.AuctionId)
	return utils.CreateMessageByBids(b)
}

type getClientBidsParam struct {
	ClientId string
}

func GetClientBids(r *bufio.Reader) []byte {
	var p getClientBidsParam
	utils.ReaderJsonTo(r, &p)
	b := repository.GetAllBidsByBidderId(p.ClientId)
	return utils.CreateMessageByBids(b)
}

type getCloseBidParam struct {
	AuctionId int
}

func GetCloseBid(r *bufio.Reader) []byte {
	var p getCloseBidParam
	utils.ReaderJsonTo(r, &p)

	a := repository.AuctionFindById(p.AuctionId)

	if a.LastBidId < 0 {
		return utils.NoBidsInAuction()
	}

	b := repository.FindBidById(a.LastBidId)

	return utils.CreateMessageByBid(b)
}

func MakeBid(r *bufio.Reader) []byte {
	var p repository.Bid
	utils.ReaderJsonTo(r, &p)

	if p.Value < 0 {
		return utils.BidInvalidMessage()
	}

	u := repository.AccountFindById(p.BidderId)

	if p.Value > u.Money {
		return utils.NotEnoughMoney()
	}

	a := repository.AuctionFindById(p.AuctionId)

	if a.IsClosed == true {
		return utils.AuctionIsClosed()
	}

	bid, _ := repository.InsertOneBid(&p)

	a.LastBidId = bid

	repository.SetLastBidAuction(a.Id, bid)

	return utils.CreateMessageBaseType(bid)
}
