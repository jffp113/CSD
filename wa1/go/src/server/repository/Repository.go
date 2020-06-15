package repository

import (
	"context"
	"errors"
	"fmt"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"log"
	"os"
	"sync"
	"time"
)

//docker run -ti --rm -v $(PWD)/mongo:/etc/mongo  -p 27017:27017 --rm mongo --bind_ip 0.0.0.0

type UserAccount struct {
	Id    string
	Money int
}
type Transaction struct {
	Id     int
	From   string
	To     string
	Amount int
}
type Counter struct {
	mu sync.Mutex
	x  int
}

func (v *UserAccount) toBSON() bson.M {
	return bson.M{"Id": v.Id, "Money": v.Money}
}

var client, _ = mongo.NewClient(options.Client().ApplyURI("mongodb://" + os.Getenv("DB")+":27017"))
var accountCollection = client.Database("CSD").Collection("Account")
var transactionCollection = client.Database("CSD").Collection("Transaction")
var auctionCollection = client.Database("CSD").Collection("Auction")
var bidCollection = client.Database("CSD").Collection("Bid")
var counter = Counter{}

func Connect() {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	log.Println(client.Connect(ctx))
}

func ChangeAccountMoney(id string, money int) error {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	_, err := accountCollection.UpdateOne(ctx, bson.M{"Id": id}, bson.M{"$set": bson.M{"Money": money}})
	return err
}

func InsertOneAccount(account *UserAccount) error {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	c, err := accountCollection.Clone()
	_, err = c.InsertOne(ctx, account.toBSON())

	if err != nil {
		fmt.Println(err)
		return err
	}

	return nil
}

func AccountFindById(id string) *UserAccount {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var t UserAccount
	c, _ := accountCollection.Clone()
	sr := c.FindOne(ctx, bson.M{"Id": id})

	if sr.Err() != nil {
		return nil
	}

	sr.Decode(&t)

	return &t
}

func (v *Transaction) toBSON() bson.M {
	return bson.M{"Id": v.Id, "From": v.From, "To": v.To, "Amount": v.Amount}
}

func TransactionFindById(id string) *Transaction {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var t Transaction
	c, _ := transactionCollection.Clone()
	sr := c.FindOne(ctx, bson.M{"Id": id})

	if sr.Err() != nil {
		return nil
	}

	sr.Decode(&t)

	return &t
}

func SaveTransactionById(id string, t *Transaction) error {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	c, _ := transactionCollection.Clone()
	_, err := c.UpdateOne(ctx, bson.M{"Id": id}, t)

	panic(errors.New("Not Implemented"))
	return err
}

func FindAllTransactions() []Transaction {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var result []Transaction
	//var curr Transaction
	cc, _ := transactionCollection.Clone()
	c, err := cc.Find(ctx, bson.D{})

	if err != nil {
		return nil
	}

	err = c.All(ctx, &result)

	if err != nil {
		return nil
	}

	return result
}

func GetTransactionsByFromOrTo(id string) []Transaction {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var result []Transaction
	cc, _ := transactionCollection.Clone()
	c, err := cc.Find(ctx, bson.M{"$or": []bson.M{{"From": id}, {"To": id}},
	})

	if err != nil {
		return nil
	}

	err = c.All(ctx, &result)

	if err != nil {
		return nil
	}

	return result
}

func InsertOneTransaction(transaction *Transaction) error {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)

	counter.mu.Lock()
	transaction.Id = counter.x
	counter.x++
	counter.mu.Unlock()

	c, _ := transactionCollection.Clone()
	_, err := c.InsertOne(ctx, transaction.toBSON())

	if err != nil {
		return err
	}

	return nil
}

//Auction Repo

type Auction struct {
	Id        int
	OwnerId   string
	LastBidId int
	IsClosed  bool
}

func (v *Auction) toBSON() bson.M {
	return bson.M{
		"Id":        v.Id,
		"OwnerId":   v.OwnerId,
		"LastBidId": v.LastBidId,
		"IsClosed":  v.IsClosed}
}

func InsertOneAuction(auction *Auction) (int, error) {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	ac, err := auctionCollection.Clone()

	if err != nil {
		return -1, err
	}

	counter.mu.Lock()
	auction.Id = counter.x
	counter.x++
	counter.mu.Unlock()

	_, err = ac.InsertOne(ctx, auction.toBSON())

	if err != nil {
		return -1, err
	}

	return auction.Id, nil
}

func AuctionFindById(id int) *Auction {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var t Auction
	c, _ := auctionCollection.Clone()
	sr := c.FindOne(ctx, bson.M{"Id": id})

	if sr.Err() != nil {
		return nil
	}

	sr.Decode(&t)

	return &t
}

func CloseOneAuction(id int) (int, error) {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	c, _ := auctionCollection.Clone()
	r, err := c.UpdateOne(ctx, bson.M{"Id": id}, bson.M{"$set": bson.M{"IsClosed": true}})

	if err != nil {
		return -1, err
	}

	return int(r.ModifiedCount), nil
}

func SetLastBidAuction(id int, lastBid int) (int, error) {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	c, _ := auctionCollection.Clone()
	r, err := c.UpdateOne(ctx, bson.M{"Id": id}, bson.M{"$set": bson.M{"LastBidId": lastBid}})

	if err != nil {
		return -1, err
	}

	return int(r.ModifiedCount), nil
}

func GetOpenAuctionIs(b bool) []Auction {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var result []Auction
	cc, _ := auctionCollection.Clone()
	c, err := cc.Find(ctx, bson.M{"IsClosed": b})

	if err != nil {
		return nil
	}

	err = c.All(ctx, &result)

	if err != nil {
		return nil
	}

	return result
}

//Bid
type Bid struct {
	Id        int
	BidderId  string
	AuctionId int
	Value     int
}

func InsertOneBid(bid *Bid) (int, error) {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	c, err := bidCollection.Clone()

	if err != nil {
		return -1, err
	}

	counter.mu.Lock()
	bid.Id = counter.x
	counter.x++
	counter.mu.Unlock()

	_, err = c.InsertOne(ctx, bid)

	if err != nil {
		return -1, err
	}

	return bid.Id, nil

}

func FindBidById(id int) *Bid {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var t Bid
	c, _ := bidCollection.Clone()
	sr := c.FindOne(ctx, bson.M{"Id": id})

	if sr.Err() != nil {
		return nil
	}

	sr.Decode(&t)

	return &t
}

func GetAllBidsByAuctionId(id int) []Bid {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var result []Bid
	cc, _ := bidCollection.Clone()
	c, err := cc.Find(ctx, bson.M{"AuctionId": id})

	if err != nil {
		return nil
	}

	err = c.All(ctx, &result)

	if err != nil {
		return nil
	}

	return result
}

func GetAllBidsByBidderId(id string) []Bid {
	ctx, _ := context.WithTimeout(context.Background(), 30*time.Second)
	var result []Bid
	cc, _ := bidCollection.Clone()
	c, err := cc.Find(ctx, bson.M{"BidderId": id})

	if err != nil {
		return nil
	}

	err = c.All(ctx, &result)

	if err != nil {
		return nil
	}

	return result
}
