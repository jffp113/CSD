package controller

import (
	"SGX_CSD_Server/src/server/repository"
	"SGX_CSD_Server/src/utils"
	"bufio"
	"log"
)

const (
	SYSTEM_RESERVED = "SYSTEM"
)

func CreateMoney(reader *bufio.Reader) []byte {
	var tran repository.Transaction
	utils.ReaderJsonTo(reader, &tran)
	log.Printf("Creating money to %s with value %d\n", tran.To, tran.Amount)

	if tran.Amount < 0 || tran.To == SYSTEM_RESERVED {
		return utils.CreateInvalidMessage()
	}

	u := repository.AccountFindById(tran.To)

	if u == nil {
		u = &repository.UserAccount{Id: tran.To}
		repository.InsertOneAccount(u)
	}

	if err := repository.ChangeAccountMoney(u.Id, u.Money+tran.Amount); err != nil {
		return utils.InternalError(err)
	}

	tran.From = SYSTEM_RESERVED
	repository.InsertOneTransaction(&tran)

	return []byte{byte(0)}
}

func TransferMoneyBetweenUsers(reader *bufio.Reader) []byte {
	var tran repository.Transaction
	utils.ReaderJsonTo(reader, &tran)

	if tran.Amount < 0 || tran.To == SYSTEM_RESERVED {
		return utils.CreateInvalidMessage()
	}

	u1 := repository.AccountFindById(tran.From)
	if u1 == nil || u1.Money < tran.Amount {
		return utils.AccountDoesNotExistOrDoesNotHaveEnoughMoneyMessage()
	}

	u2 := repository.AccountFindById(tran.To)
	if u2 == nil {
		return utils.AccountDoesNotExistOrDoesNotHaveEnoughMoneyMessage()
	}

	repository.ChangeAccountMoney(u1.Id, u1.Money-tran.Amount)
	repository.ChangeAccountMoney(u2.Id, u2.Money+tran.Amount)

	repository.InsertOneTransaction(&tran)

	return []byte{byte(0)}
}

type currentAmountParameters struct {
	UserId string
}

func CurrentAmount(reader *bufio.Reader) []byte {
	var userparam currentAmountParameters
	utils.ReaderJsonTo(reader, &userparam)
	u := repository.AccountFindById(userparam.UserId)

	if u == nil {
		return utils.AccountDoesNotExistOrDoesNotHaveEnoughMoneyMessage()
	}

	return utils.CreateMessageBaseType(u.Money)
}

func LedgerOfClientTransfers(reader *bufio.Reader) []byte {
	tran := repository.FindAllTransactions()
	return utils.CreateMessageByTransactions(tran)
}

func LedgerOfClientTransfersById(reader *bufio.Reader) []byte {
	var userId string
	utils.ReaderJsonTo(reader, &userId)

	tran := repository.GetTransactionsByFromOrTo(userId)
	return utils.CreateMessageByTransactions(tran)
}

//Erro 1 -> mensagem
//Caso 0 -> Json
