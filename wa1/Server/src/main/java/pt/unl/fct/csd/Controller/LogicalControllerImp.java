package pt.unl.fct.csd.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.unl.fct.csd.Replication.ClientAsyncReplicator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;

@PropertySource("classpath:application.properties")
@Service("LogicalEndpoint")
public class LogicalControllerImp implements LogicalController {

    @Value("${logic.address}")
    private String address;

    @Value("${logic.port}")
    private int port;

    private final Logger logger =
            LoggerFactory.getLogger(ClientAsyncReplicator.class);

    @Override
    public byte[] CallOperation(byte[] val) {
        logger.info("Call Operation " + Arrays.hashCode(val));
        try (Socket socket = new Socket(address, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {
            out.write(val);
            return in.readAllBytes();

        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}
