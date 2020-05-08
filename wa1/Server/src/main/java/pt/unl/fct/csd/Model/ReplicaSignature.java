package pt.unl.fct.csd.Model;

public class ReplicaSignature {

    private final int replicaNumber;

    private final byte[] signature;

    public ReplicaSignature(int replicaNumber, byte[] signature) {
        this.replicaNumber = replicaNumber;
        this.signature = signature;
    }
}
