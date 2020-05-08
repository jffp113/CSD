package pt.unl.fct.csd.Models;

import pt.unl.fct.csd.Models.ReplicaSignature;

import java.util.Collection;

public class SystemReply {

    private byte[] reply;

    private Collection<ReplicaSignature> signatures;

    public SystemReply(byte[] reply, Collection<ReplicaSignature> signatures) {
        this.reply = reply;
        this.signatures = signatures;
    }

    public SystemReply() {}

    public byte[] getReply() {
        return reply;
    }

    public Collection<ReplicaSignature> getSignatures() {
        return signatures;
    }

    public void setReply(byte[] reply) {
        this.reply = reply;
    }

    public void setSignatures(Collection<ReplicaSignature> signatures) {
        this.signatures = signatures;
    }
}
