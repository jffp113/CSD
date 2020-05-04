package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.VoidWrapper;
import pt.unl.fct.csd.Replication.ClientReplicator;
import pt.unl.fct.csd.Replication.DualArgReplication;
import pt.unl.fct.csd.Replication.InvokerWrapper;
import pt.unl.fct.csd.Replication.Path;

import java.util.List;

@PropertySource("classpath:application.properties")
@RestController("ImpSmartReplicator")
@RequestMapping(value = SmartContractController.BASE_URL)
public class SmartContractControllerReplicatorImp implements SmartContractController {
    private final Logger logger =
            LoggerFactory.getLogger(SmartContractControllerReplicatorImp.class);

    @Autowired
    ClientReplicator clientReplicator;

    @Override
    public void createSmart(String token, byte[] smartContract) {
        logger.info("Proxy received request createSmartContract");
        InvokerWrapper<VoidWrapper> result =
                clientReplicator.invokeOrderedReplication(
                        new DualArgReplication<>(token,smartContract),Path.CREATE_SMART);
        result.getResultOrThrow();
    }

    @Override
    public List<String> ledgerSmartContracts() {
        logger.info("Proxy received request ledgerSmartContracts");
        return new GenericListResults<String, String>(clientReplicator)
                .getListWithPath(Path.LIST_SMART);
    }

    @Override
    public void deleteSmartContract(String token) {
        logger.info("Proxy received request deleteSmartContract");
        InvokerWrapper<VoidWrapper> result =
                clientReplicator.invokeOrderedReplication(token,Path.REMOVE_SMART);
        result.getResultOrThrow();
    }

    @Override
    public byte[] getSmartContract(String token) {
        logger.info("Proxy received request getSmartContract");
        InvokerWrapper<byte[]> result =
                clientReplicator.invokeOrderedReplication(token,Path.GET_SMART);

        return result.getResultOrThrow();
    }
}
