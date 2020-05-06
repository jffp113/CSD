package pt.unl.fct.csd.Replication;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.RequestContext;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Model.ReplyChain;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ClientAsynchReplicator {
    private final Logger logger =
            LoggerFactory.getLogger(ClientAsynchReplicator.class);

    @Autowired
    AsynchServiceProxy asynchSP;

    public <V extends Serializable,E> List<AsyncReply<InvokerWrapper<V>>> invokeOrderedReplication(E object, Path path) throws InterruptedException {
        logger.info("Start invoking async replication");
        BlockingQueue<List<AsyncReply<InvokerWrapper<V>>>> replyChain = new LinkedBlockingDeque<>();

        asynchSP.invokeAsynchRequest(convertInput(object, path), new ReplyListenerImp(replyChain,asynchSP),
                TOMMessageType.ORDERED_REQUEST);

        return replyChain.take();
    }

    private <E> byte[] convertInput (E object, Path path) {
        try {
            return tryToConvertInput(object, path);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private <E> byte[] tryToConvertInput (E object, Path path) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutput objOut = new ObjectOutputStream(byteOut)) {

            objOut.writeObject(path);
            objOut.writeObject(object);

            objOut.flush();
            byteOut.flush();
            return byteOut.toByteArray();
        }
    }

}
