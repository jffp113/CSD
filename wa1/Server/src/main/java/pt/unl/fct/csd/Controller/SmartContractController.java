package pt.unl.fct.csd.Controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public interface SmartContractController {

    String BASE_URL = "/smart";
    String CREATE_SMART = "/create/{token}";
    String LIST_SMART = "/list";
    String REMOVE_SMART = "/remove/{token}";


    @PostMapping(
            value = CREATE_SMART,
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    void createSmart(@PathVariable("token") String token,@RequestBody byte[] smartContract);

    @GetMapping(
            value = LIST_SMART,
            produces = APPLICATION_JSON_VALUE)
    List<String> ledgerSmartContracts();

    @DeleteMapping(
            value = REMOVE_SMART
    )
    void deleteSmartContract(@PathVariable("token") String token);

    byte[] getSmartContract(String token);

}

