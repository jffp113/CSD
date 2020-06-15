nc ns31249243.ip-51-210-0.eu 4678
GET_MONEY
{
"userId": "tiago"
}

nc ns31249243.ip-51-210-0.eu 4678
CREATE_MONEY
{
"to": "tiago",
"amount": 100
}

nc ns31249243.ip-51-210-0.eu 4678
CREATE_AUCTION
{
  "ownerId": "tiago"
}

nc ns31249243.ip-51-210-0.eu 4678
GET_OPEN_AUCTIONS

nc ns31249243.ip-51-210-0.eu 4678
GET_CLOSED_AUCTIONS

nc ns31249243.ip-51-210-0.eu 4678
TERMINATE_AUCTION
{
  "auctionId": 0
}


#docker run --rm -d --device=/dev/isgx -p 4678:4678 -e SCONE_VERSION=1 -e SCONE_HEAP=2G -e SCONE_LOG=7  test
#docker run -ti --rm -p 27017:27017  mongo --bind_ip 0.0.0.0