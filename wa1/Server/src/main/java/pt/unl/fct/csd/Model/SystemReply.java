package pt.unl.fct.csd.Model;

import java.util.Collection;
import java.util.Set;

public class SystemReply {

    private final byte[] reply;

    private final Collection<ReplicaSignature> signatures;

    public SystemReply(byte[] reply, Collection<ReplicaSignature> signatures) {
        this.reply = reply;
        this.signatures = signatures;
    }

    public byte[] getReply() {
        return reply;
    }

    public Collection<ReplicaSignature> getSignatures() {
        return signatures;
    }
}
