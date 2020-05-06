package pt.unl.fct.csd.Replication;

import bftsmart.communication.client.ReplyListener;
import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.RequestContext;
import bftsmart.tom.core.messages.TOMMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ReplyListenerImp<V extends Serializable> implements ReplyListener {

    private final Logger logger =
            LoggerFactory.getLogger(ClientAsynchReplicator.class);

    int repliesCounter = 0;
    final List<AsyncReply<InvokerWrapper<V>>> replies = new LinkedList<>();

    BlockingQueue<List<AsyncReply<InvokerWrapper<V>>>> replyChain;

    AsynchServiceProxy asynchSP;


    public ReplyListenerImp(BlockingQueue<List<AsyncReply<InvokerWrapper<V>>>> replyChain, AsynchServiceProxy asynchSP) {
        this.replyChain = replyChain;
        this.asynchSP = asynchSP;
    }

    @Override
        public void replyReceived(RequestContext requestContext, TOMMessage msg) {
        logger.info("Invoked replyReceived");
        replies.add(new AsyncReply<>(msg.getSender(), parseReplicationReply(msg.getContent())));

        double quorum = (Math.ceil((double) (asynchSP.getViewManager().getCurrentViewN() + //4
                asynchSP.getViewManager().getCurrentViewF() + 1) / 2.0));
        if(++repliesCounter >= quorum) {
            logger.info("Enough replies received");

            replyChain.add(replies);
            asynchSP.cleanAsynchRequest(requestContext.getOperationId());
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
