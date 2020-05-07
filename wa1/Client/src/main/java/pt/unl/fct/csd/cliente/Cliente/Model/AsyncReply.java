package pt.unl.fct.csd.cliente.Cliente.Model;

public class AsyncReply {
    private int senderId;
    private byte[] reply;
    private byte[] signedReply;

    public AsyncReply(){}

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getSenderId(){
        return senderId;
    }

    public void setReply(byte[] reply) {
        this.reply = reply;
    }

    public byte[] getReply(){
        return reply;
    }

    public void setSignedReply(byte[] signedReply) {
        this.signedReply = signedReply;
    }

    public byte[] getSignedReply() {
        return signedReply;
    }
}
