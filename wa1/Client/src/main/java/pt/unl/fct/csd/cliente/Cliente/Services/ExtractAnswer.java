package pt.unl.fct.csd.cliente.Cliente.Services;

import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.cliente.Cliente.Model.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.Model.SystemReply;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

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
        return extractAnswer(base + ORDERED_REQ, path, postJson);
    }

    public String extractUnorderedAnswer(String path, String postJson) throws ServerAnswerException {
        return extractAnswer(base + UNORDERED_REQ, path, postJson);
    }

    private String extractAnswer(String url, String path, String postJson) throws ServerAnswerException {
        String objPost = String.format("%s\n%s", path, postJson);
        System.out.println(objPost);
        ResponseEntity<SystemReply> response =
                restTemplate.postForEntity(url, objPost.getBytes(StandardCharsets.UTF_8), SystemReply.class);

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
        byte[] result = Arrays.copyOfRange(answer,1,answer.length);
        String resultAsString = new String(result);
        if(answer[0] == 0){
            return InvokerWrapper.newInvokeWrapperResult(resultAsString);
        }
        return InvokerWrapper.newInvokeWrapperException(resultAsString);
    }
}