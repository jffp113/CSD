package pt.unl.fct.csd.Replication;

public class AsyncReply<V> {
    private final int senderId;
    private final V reply;

    public AsyncReply(int serverId, V reply){
        this.senderId = serverId;
        this.reply = reply;
    }

    public int getSenderId(){
        return senderId;
    }

    public V getReply(){
        return reply;
    }
}
