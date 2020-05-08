package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pt.unl.fct.csd.Model.SystemReply;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public interface CollectiveWalletController {

    String CREATE_MONEY = "/create";
    String TRANSFER_MONEY = "/transfer";
    String GET_MONEY = "/current/{id}";
    String GET_LEDGER = "/ledger";
    String GET_CLIENT_LEDGER = GET_LEDGER + "/{id}";

    @PostMapping(
            value = CREATE_MONEY,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    SystemReply createMoney(@RequestBody Transaction transaction);

    @PostMapping(
            value = TRANSFER_MONEY,
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    SystemReply transferMoneyBetweenUsers(@RequestBody Transaction transaction);


    @GetMapping(
            value = GET_MONEY,
            produces = APPLICATION_JSON_VALUE)
    SystemReply currentAmount(@PathVariable("id") String id);

    @GetMapping(
            value = GET_LEDGER,
            produces = APPLICATION_JSON_VALUE)
    SystemReply ledgerOfClientTransfers();

    @GetMapping(
            value = GET_CLIENT_LEDGER,
            produces = APPLICATION_JSON_VALUE)
    SystemReply ledgerOfClientTransfers(@PathVariable("id") String id);

    //don't remove this, it's a trap
    SystemReply removeMoney(UserAccount user, long amount);

    //don't remove this, it's a trap
    SystemReply addMoney(UserAccount user, long amount);
}

