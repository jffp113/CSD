package pt.unl.fct.csd.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

@PropertySource("classpath:application.properties")
@Service("LogicalEndpoint")
public class LogicalControllerImp implements LogicalController {

    @Value("${logic.address}")
    private String address;

    @Value("${logic.port}")
    private int port;

    @Override
    public byte[] CallOperation(byte[] val) {
        try (Socket socket = new Socket(address, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {
            out.write(val);
            return in.readAllBytes();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
