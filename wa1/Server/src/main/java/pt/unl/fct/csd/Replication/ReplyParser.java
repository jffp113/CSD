package pt.unl.fct.csd.Replication;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ReplyParser {

    private byte[] reply = new byte[0];

    private byte[] signedReply = new byte[0];

    public ReplyParser(byte[] fullReply) {
        // fullReply.length always > 0 because at least a signature is provided
        try (ByteArrayInputStream in = new ByteArrayInputStream(fullReply);
             DataInputStream inReader = new DataInputStream(in)) {
            int replyLength = inReader.readInt();
            reply = inReader.readNBytes(replyLength);
            int signedLength = inReader.readInt();
            signedReply = inReader.readNBytes(signedLength);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getReply() {
        return reply;
    }

    public byte[] getSignedReply() {
        return getSignedReply();
    }

}
