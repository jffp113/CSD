package pt.unl.fct.csd.Model;

import bftsmart.tom.core.messages.TOMMessage;
import pt.unl.fct.csd.Replication.ReplyParser;

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
