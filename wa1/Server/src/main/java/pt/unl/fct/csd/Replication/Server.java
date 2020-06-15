package pt.unl.fct.csd.Replication;

import bftsmart.reconfiguration.util.RSAKeyLoader;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import bftsmart.tom.util.TOMUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Controller.LogicalController;
import pt.unl.fct.csd.Controller.ServerController;
import pt.unl.fct.csd.Model.*;

import javax.annotation.PostConstruct;
import java.io.*;

@PropertySource("classpath:application.properties")
@Component
public class Server extends DefaultSingleRecoverable implements Runnable {
    private static final String DEFAULT_KEY_CONFIG = "";

    private final Logger logger =
            LoggerFactory.getLogger(Server.class);

    @Value("${replica.id}")
    private int ID;

    @Qualifier("LogicalEndpoint")
    @Autowired
    private LogicalController logicalController;

    private RSAKeyLoader keyLoader;

    @PostConstruct
    public void init() {
        new ServiceReplica(ID, this, this);
        this.keyLoader = new RSAKeyLoader(ID, DEFAULT_KEY_CONFIG);
    }

    @Override
    public void run() {
        new ServiceReplica(ID, this, this);
    }

    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext messageContext) {
        return sendFullReply(logicalController.CallOperation(command));
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext messageContext) {
        return sendFullReply(logicalController.CallOperation(command));
    }

    private byte[] sendFullReply(byte[] serverReply) {
        logger.info("sendFullReply: reply len=" + serverReply.length);
        try (ByteArrayOutputStream bS = new ByteArrayOutputStream();
             DataOutputStream dS = new DataOutputStream(bS)) {
            return tryToSendFullReply(serverReply, bS, dS);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private byte[] tryToSendFullReply(byte[] serverReply, ByteArrayOutputStream bS, DataOutputStream dS)
            throws Exception {
        dS.writeInt(serverReply.length);
        dS.write(serverReply);
        byte[] signedAnswer = sign(serverReply);
        logger.info("tryToSendFullReply: signed reply len=" + signedAnswer.length);
        dS.writeInt(signedAnswer.length);
        dS.write(signedAnswer);
        return bS.toByteArray();
    }

    private byte[] sign(byte[] serverReply) throws Exception {
        return TOMUtil.signMessage(keyLoader.loadPrivateKey(), serverReply);
    }

    @Override
    public void installSnapshot(byte[] bytes) {
        logger.error("Not implemented");
    }

    @Override
    public byte[] getSnapshot() {
        logger.error("Not implemented");
        return new byte[0];
    }

}