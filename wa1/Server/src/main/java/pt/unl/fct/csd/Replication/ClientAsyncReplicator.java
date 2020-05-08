package pt.unl.fct.csd.Replication;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.core.messages.TOMMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Model.AsyncReply;
import pt.unl.fct.csd.Model.SystemReply;

import java.io.*;
import java.util.List;
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

    public SystemReply invokeUnorderedReplication(Path path) {
        return invokeUnorderedReplication(null, path);
    }

    public <E> SystemReply invokeUnorderedReplication(E object, Path path) {
        return invoke(object, path, UNORDERED_REQUEST);
    }

    public SystemReply invokeOrderedReplication(Path path) {
        return invokeOrderedReplication(null, path);
    }

    public <E> SystemReply invokeOrderedReplication(E object, Path path) {
        return invoke(object, path, ORDERED_REQUEST);
    }

    private <E> SystemReply invoke(E object, Path path, TOMMessageType type) {
        try {
            return tryToInvoke(object, path, type);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private <E> SystemReply tryToInvoke(E object, Path path, TOMMessageType type) throws InterruptedException {
        logger.info("Start invoking async replication");
        BlockingQueue<SystemReply> replyChain = new LinkedBlockingDeque<>();
        byte[] request = convertInput(object, path);
        ReplyListener replyListener = new ReplyListenerImp(replyChain, asyncSP);
        asyncSP.invokeAsynchRequest(request, replyListener, type);
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
