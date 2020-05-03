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

    public <V,E> ReplyChain<V> invokeOrderedReplication(E object, Path path) throws InterruptedException {
        logger.info("Start invoking async replication");
        BlockingQueue<List<AsyncReply<V>>> replyChain = new LinkedBlockingDeque<>();

        asynchSP.invokeAsynchRequest(convertInput(object, path), new ReplyListener() {

            int repliesCounter = 0;
            final List<AsyncReply<V>> replies = new LinkedList<>();

            @Override
            public void replyReceived(RequestContext requestContext, TOMMessage msg) {
                replies.add(new AsyncReply<>(msg.getSender(), parseReplicationReply(msg.getContent())));

                double quorum = (Math.ceil((double) (asynchSP.getViewManager().getCurrentViewN() +
                        asynchSP.getViewManager().getCurrentViewF() + 1) / 2.0));
                if(++repliesCounter >= quorum) {
                    logger.info("Enough replies received");

                    replyChain.add(replies);
                    asynchSP.cleanAsynchRequest(requestContext.getOperationId());
                }
            }
        }, TOMMessageType.ORDERED_REQUEST);

        return new ReplyChain<>(replyChain.take());
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

    private <V> V parseReplicationReply (byte[] replicationReply) {
        try {
            return tryToParseReplicationReply(replicationReply);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <V> V tryToParseReplicationReply (byte[] replicationReply)
            throws IOException, ClassNotFoundException {
        if (replicationReply == null || replicationReply.length == 0)
            return null;
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(replicationReply);
             ObjectInput objIn = new ObjectInputStream(byteIn)) {
            return (V) objIn.readObject();
        }
    }


}
