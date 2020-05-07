package pt.unl.fct.csd.Replication;

import bftsmart.tom.core.messages.TOMMessage;

public class AsyncReply<V> {
    private final int senderId;
    private final byte[] reply;
    private final byte[] signedReply;

    public AsyncReply(TOMMessage msg){
        this.senderId = msg.getSender();
        this.reply = msg.getContent();
        this.signedReply = msg.serializedMessage;
    }

    public int getSenderId(){
        return senderId;
    }

    public byte[] getReply(){
        return reply;
    }
}
