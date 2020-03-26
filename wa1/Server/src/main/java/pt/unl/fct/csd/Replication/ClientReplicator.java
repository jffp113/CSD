package pt.unl.fct.csd.Replication;

import bftsmart.tom.ServiceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Controller.WalletControllerReplicatorImp;

import java.io.*;

@Service
public class ClientReplicator {
    private final Logger logger =
            LoggerFactory.getLogger(ClientReplicator.class);

    @Autowired
    ServiceProxy serviceProxy;

    public <V,E> V invokeReplication(E object,Path path){
        logger.info("Start invoking replication");
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutput objOut = new ObjectOutputStream(byteOut);) {

            objOut.writeObject(path);
            objOut.writeObject(object);

            objOut.flush();
            byteOut.flush();

            byte[] reply = serviceProxy.invokeOrdered(byteOut.toByteArray());
            logger.info("Reply has size: " + reply.length);
            if (reply.length == 0)
                return null;
            try (ByteArrayInputStream byteIn = new ByteArrayInputStream(reply);
                 ObjectInput objIn = new ObjectInputStream(byteIn)) {
                return (V)objIn.readObject();
            }


        } catch (IOException | ClassNotFoundException e) {
           e.printStackTrace();
        }

        return null;
    }

}
