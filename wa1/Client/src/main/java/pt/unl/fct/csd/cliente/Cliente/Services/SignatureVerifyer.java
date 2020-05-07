package pt.unl.fct.csd.cliente.Cliente.Services;


import bftsmart.reconfiguration.util.RSAKeyLoader;
import bftsmart.tom.util.TOMUtil;
import pt.unl.fct.csd.cliente.Cliente.Model.AsyncReply;

import java.security.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SignatureVerifyer {

    private static final String KEYS_DEFAULT_LOAD_PATH = "";

    public static List<byte[]> getVerifiedReplies(List<AsyncReply> replies) {
        return replies.stream().filter(SignatureVerifyer::isValid)
                .map(AsyncReply::getReply)
                .collect(toList());
    }

    private static boolean isValid(AsyncReply reply) {
        try {
            return tryToCheckIfValid(reply);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean tryToCheckIfValid(AsyncReply reply) throws Exception {
        PublicKey key = new RSAKeyLoader(reply.getSenderId(), KEYS_DEFAULT_LOAD_PATH).
                loadPublicKey();
        return TOMUtil.verifySignature(key, reply.getReply(), reply.getSignedReply());
    }

}
