package pt.unl.fct.csd.cliente.Cliente.Services;

import com.google.gson.Gson;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.cliente.Cliente.Model.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.Model.SystemReply;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

public class ExtractAnswer {

    public String extractAnswerGet (String url, RestTemplate restTemplate) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.getForEntity(url, SystemReply.class);
        return extractFromResponse(response);
    }

    public <V> String extractAnswerPost (String url, V objPost,RestTemplate restTemplate) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.postForEntity(url, objPost, SystemReply.class);
        return extractFromResponse(response);
    }

    public String extractAnswerPut (String url,RestTemplate restTemplate) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.exchange(url, HttpMethod.PUT, null, SystemReply.class);
        return extractFromResponse(response);
    }

    private String extractFromResponse (ResponseEntity<SystemReply> response) throws ServerAnswerException {
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
