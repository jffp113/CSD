package pt.unl.fct.csd.cliente.Cliente.Services;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.cliente.Cliente.Model.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.Model.SystemReply;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

public class ExtractAnswer {

    private static final String ORDERED_REQ = "/ordered";
    private static final String UNORDERED_REQ = "/unordered";

    private final String base;
    private final RestTemplate restTemplate;

    public ExtractAnswer(String base, RestTemplate restTemplate) {
        this.base = base;
        this.restTemplate = restTemplate;
    }

    private static String makeOrderedUrl(String base) {
        return String.format("%s%s", base, ORDERED_REQ);
    }

    private static String makeUnorderedUrl(String base) {
        return String.format("%s%s", base, UNORDERED_REQ);
    }

    private static String makePostJson(String path, String json) {
        return String.format("%s\n%s", path, json);
    }

    public String extractOrderedAnswer(String path, String postJson) throws ServerAnswerException {
        return extractAnswer(base + ORDERED_REQ, postJson);
    }

    public String extractUnorderedAnswer(String path, String postJson) throws ServerAnswerException {
        return extractAnswer(base + UNORDERED_REQ, postJson);
    }

    private String extractAnswer(String url, String objPost) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.postForEntity(url, objPost.getBytes(), SystemReply.class);
        SystemReply systemReply = response.getBody();
        assert systemReply != null;
        try {
            if (!SignatureVerifier.isValidReply(systemReply))
                throw new NoMajorityAnswerException();
            return convertMostFrequentAnswer(systemReply.getReply()).getResultOrThrow();
        } catch (Exception e) {
            throw new ServerAnswerException(e.getMessage());
        }
    }

    private InvokerWrapper convertMostFrequentAnswer(byte[] answer) {
        return new Gson().fromJson(new String(answer), InvokerWrapper.class);
    }
}