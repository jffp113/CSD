package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.*;
import pt.unl.fct.csd.Model.Transaction;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = WalletController.BASE_URL)
public interface WalletController {

    String BASE_URL = "/money";
    String CREATE_MONEY = "/create";
    String TRANSFER_MONEY = "/transfer";
    String GET_MONEY = "/current/{id}";
    String GET_LEDGER = "/ledger";
    String GET_CLIENT_LEDGER = GET_LEDGER + "/{id}";
    /**
     * This endpoint allows to create money for
     * a certain user specified on the Transaction
     * @param transaction Transaction includes the information necessary to give
     * a user money.
     * @return Nothing if every thing goes ok, else returns a exception
     */
    @PostMapping(
            value = CREATE_MONEY,
            consumes = APPLICATION_JSON_VALUE)
    void createMoney(@RequestBody Transaction transaction);

    /**
     * This endpoint allows to transfer money from
     * a certain user to a other user on the Transaction
     * @param transaction Transaction includes the information necessary to give
     * to transfer a user do other user.
     * @return Nothing if every thing goes ok, else returns a exception
     */
    @PostMapping(
            value = TRANSFER_MONEY,
            consumes = APPLICATION_JSON_VALUE)
    void transferMoney(@RequestBody Transaction transaction);

    /**
     * //TODO
     * @param id
     * @return
     */
    @GetMapping(
            value = GET_MONEY,
            produces = APPLICATION_JSON_VALUE)
    Long currentAmount(@PathVariable("id") String id);

    @GetMapping(
            value = GET_LEDGER,
            produces = APPLICATION_JSON_VALUE)
    List<Transaction> ledgerOfClientTransfers();

    @GetMapping(
            value = GET_CLIENT_LEDGER,
            produces = APPLICATION_JSON_VALUE)
    List<Transaction> ledgerOfClientTransfers(@PathVariable("id") String id);
}

