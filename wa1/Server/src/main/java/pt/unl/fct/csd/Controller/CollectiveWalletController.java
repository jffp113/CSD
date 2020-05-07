package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Replication.AsyncReply;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public interface CollectiveWalletController {

    String CREATE_MONEY = "/create";
    String TRANSFER_MONEY = "/transfer";
    String GET_MONEY = "/current/{id}";
    String GET_LEDGER = "/ledger";
    String GET_CLIENT_LEDGER = GET_LEDGER + "/{id}";

    @PostMapping(
            value = CREATE_MONEY,
            consumes = APPLICATION_JSON_VALUE)
    List<AsyncReply> createMoney(@RequestBody Transaction transaction);

    @PostMapping(
            value = TRANSFER_MONEY,
            consumes = APPLICATION_JSON_VALUE)
    List<AsyncReply> transferMoneyBetweenUsers(@RequestBody Transaction transaction);


    @GetMapping(
            value = GET_MONEY,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> currentAmount(@PathVariable("id") String id);

    @GetMapping(
            value = GET_LEDGER,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> ledgerOfClientTransfers();

    @GetMapping(
            value = GET_CLIENT_LEDGER,
            produces = APPLICATION_JSON_VALUE)
    List<AsyncReply> ledgerOfClientTransfers(@PathVariable("id") String id);

    //don't remove this, it's a trap
    List<AsyncReply> removeMoney(UserAccount user, long amount);

    //don't remove this, it's a trap
    List<AsyncReply> addMoney(UserAccount user, long amount);
}

