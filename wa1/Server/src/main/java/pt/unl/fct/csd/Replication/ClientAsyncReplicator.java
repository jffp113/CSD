package pt.unl.fct.csd.Replication;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.core.messages.TOMMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ClientAsyncReplicator {
    private final Logger logger =
            LoggerFactory.getLogger(ClientAsyncReplicator.class);

    @Autowired
    AsynchServiceProxy asyncSP;

    public <E> List<AsyncReply> invokeOrderedReplication(E object, Path path) throws InterruptedException {
        logger.info("Start invoking async replication");
        BlockingQueue<List<AsyncReply>> replyChain = new LinkedBlockingDeque<>();
        byte[] request = convertInput(object, path);
        ReplyListener replyListener = new ReplyListenerImp(replyChain, asyncSP);
        asyncSP.invokeAsynchRequest(request, replyListener, TOMMessageType.ORDERED_REQUEST);
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
