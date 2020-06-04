package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.unl.fct.csd.Model.SystemReply;
import pt.unl.fct.csd.Replication.ClientAsyncReplicator;

@RestController("ServerEndpoint")
@RequestMapping(value = ServerController.BASE_URL)
public class ServerControllerImp implements ServerController {
    private final Logger logger =
            LoggerFactory.getLogger(ServerControllerImp.class);

    @Autowired
    ClientAsyncReplicator clientAsyncReplicator;

    @Override
    public SystemReply orderedOperation(byte[] val) {
        logger.info("Proxy received OrderedOperation");
        return clientAsyncReplicator.invokeOrderedReplication(val);
    }

    @Override
    public SystemReply unorderedOperation(byte[] val) {
        logger.info("Proxy received UnorderedOperation");
        return clientAsyncReplicator.invokeUnorderedReplication(val);
    }
}
