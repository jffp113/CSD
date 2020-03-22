package pt.unl.fct.csd.Replication;

import bftsmart.tom.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Controller.WalletControllerReplicatorImp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

@Service
public class ClientReplicator {
    private final Logger logger =
            LoggerFactory.getLogger(ClientReplicator.class);

    @Autowired
    ServiceProxy serviceProxy;

    public <T,E> T invokeReplication(E object,Path path, Invoker<T> invoker){
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutput objOut = new ObjectOutputStream(byteOut);) {

            objOut.writeObject(path);
            objOut.writeObject(object);

            objOut.flush();
            byteOut.flush();
            serviceProxy.invokeOrdered(byteOut.toByteArray());
            return invoker.doStuff();

        } catch (IOException err) {
            logger.error("Exception replicating " ,err.toString());
        }

        //serviceProxy.
        //if (reply.length == 0)
        //    return;
        //try (ByteArrayInputStream byteIn = new ByteArrayInputStream(reply);
        //     ObjectInput objIn = new ObjectInputStream(byteIn)) {
        //    return (V)objIn.readObject();
        //}
        return null;
    }

}
