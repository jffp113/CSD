package pt.unl.fct.csd.Controller;

import pt.unl.fct.csd.Replication.ClientReplicator;
import pt.unl.fct.csd.Replication.InvokerWrapper;
import pt.unl.fct.csd.Replication.Path;

import java.util.Arrays;
import java.util.List;

public class GenericListResults<E, V> {

    private ClientReplicator clientReplicator;

    public GenericListResults(ClientReplicator clientReplicator) {
        this.clientReplicator = clientReplicator;
    }

    public List<E> getListWithPath (Path path) {
        return getListWithPath(null, path);
    }

    public List<E> getListWithPath (V args, Path path) {
        InvokerWrapper<E[]> auctions = clientReplicator.
                invokeReplication(args, path);
        return Arrays.asList(auctions.getResultOrThrow());
    }
}
