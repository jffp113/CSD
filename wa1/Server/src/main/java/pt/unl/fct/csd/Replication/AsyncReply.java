package pt.unl.fct.csd.Replication;

import bftsmart.tom.core.messages.TOMMessage;

public class AsyncReply {
    private final int senderId;
    private final byte[] reply;
    private final byte[] signedReply;

    public AsyncReply(int senderId, ReplyParser parser){
        this.senderId = senderId;
        this.reply = parser.getReply();
        this.signedReply = parser.getSignedReply();
    }

    public int getSenderId(){
        return senderId;
    }

    public byte[] getReply(){
        return reply;
    }

    public byte[] getSignedReply() {
        return signedReply;
    }
}
