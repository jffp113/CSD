package pt.unl.fct.csd.Model;

import java.util.Collection;
import java.util.Set;

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

    public void setReply(byte[] reply) {
        this.reply = reply;
    }

    public Collection<ReplicaSignature> getSignatures() {
        return signatures;
    }

    public void setSignatures(Collection<ReplicaSignature> signatures) {
        this.signatures = signatures;
    }
}
