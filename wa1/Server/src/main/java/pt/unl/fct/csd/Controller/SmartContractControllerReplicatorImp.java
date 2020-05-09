package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.SystemReply;
import pt.unl.fct.csd.Model.VoidWrapper;
import pt.unl.fct.csd.Replication.*;

import java.util.List;

@PropertySource("classpath:application.properties")
@RestController("ImpSmartReplicator")
@RequestMapping(value = CollectiveSmartContractController.BASE_URL)
public class SmartContractControllerReplicatorImp implements CollectiveSmartContractController {
    private final Logger logger =
            LoggerFactory.getLogger(SmartContractControllerReplicatorImp.class);

    @Autowired
    ClientAsyncReplicator asyncReplicator;

    @Override
    public SystemReply createSmart(String token, byte[] smartContract) {
        DualArgReplication<String, byte[]> arguments =
                new DualArgReplication<>(token,smartContract);
        return asyncReplicator.invokeOrderedReplication(arguments, Path.CREATE_SMART);
    }

    @Override
    public SystemReply ledgerSmartContracts() {
        logger.info("Proxy received request ledgerSmartContracts");
        return asyncReplicator.invokeUnorderedReplication(Path.LIST_SMART);
    }

    @Override
    public SystemReply deleteSmartContract(String token) {
        logger.info("Proxy received request deleteSmartContract");
        return asyncReplicator.invokeOrderedReplication(token, Path.REMOVE_SMART);
    }

    @Override
    public SystemReply getSmartContract(String token) {
        logger.info("Proxy received request getSmartContract");
        return asyncReplicator.invokeUnorderedReplication(token, Path.GET_SMART);
    }
}
