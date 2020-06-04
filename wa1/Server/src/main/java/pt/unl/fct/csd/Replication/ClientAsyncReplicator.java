package pt.unl.fct.csd.Replication;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.core.messages.TOMMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Model.SystemReply;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static bftsmart.tom.core.messages.TOMMessageType.ORDERED_REQUEST;
import static bftsmart.tom.core.messages.TOMMessageType.UNORDERED_REQUEST;

@Service
public class ClientAsyncReplicator {
    private final Logger logger =
            LoggerFactory.getLogger(ClientAsyncReplicator.class);

    @Autowired
    AsynchServiceProxy asyncSP;

    public <E> SystemReply invokeUnorderedReplication(E object) {
        return invoke(object, UNORDERED_REQUEST);
    }

    public <E> SystemReply invokeOrderedReplication(E object) {
        return invoke(object, ORDERED_REQUEST);
    }

    private <E> SystemReply invoke(E object, TOMMessageType type) {
        try {
            return tryToInvoke(object, type);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private <E> SystemReply tryToInvoke(E object, TOMMessageType type) throws InterruptedException {
        logger.info("Start invoking async replication");
        BlockingQueue<SystemReply> replyChain = new LinkedBlockingDeque<>();
        byte[] request = (byte[]) object; // TODO change this, it's technically correct but destroys the generic methods
        ReplyListener replyListener = new ReplyListenerImp(replyChain, asyncSP);
        asyncSP.invokeAsynchRequest(request, replyListener, type);
        return replyChain.take();
    }


}
