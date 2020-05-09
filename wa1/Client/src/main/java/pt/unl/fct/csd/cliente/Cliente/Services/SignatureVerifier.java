package pt.unl.fct.csd.cliente.Cliente.Services;


import bftsmart.reconfiguration.util.RSAKeyLoader;
import bftsmart.tom.util.TOMUtil;
import pt.unl.fct.csd.cliente.Cliente.Model.ReplicaSignature;
import pt.unl.fct.csd.cliente.Cliente.Model.SystemReply;

import java.security.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SignatureVerifier {

    private static final String KEYS_DEFAULT_LOAD_PATH = "";

    public static boolean isValidReply(SystemReply systemReply) {
        byte[] reply = systemReply.getReply();
        return systemReply.getSignatures().stream()
                .filter(sign -> isValid(sign, systemReply.getReply()))
                .count() >= 3; //TODO not static quorum size;
    }

    private static boolean isValid(ReplicaSignature signature, byte[] reply) {
        try {
            return tryToCheckIfValid(signature, reply);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean tryToCheckIfValid(ReplicaSignature signature, byte[] reply)
            throws Exception {
        PublicKey key = new RSAKeyLoader(signature.getReplicaNumber(), KEYS_DEFAULT_LOAD_PATH).
                loadPublicKey();
        return TOMUtil.verifySignature(key, reply, signature.getSignature());
    }

}
