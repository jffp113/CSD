package pt.unl.fct.csd.cliente.Cliente.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.cliente.Cliente.Model.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.Model.SystemReply;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

public class ExtractAnswer<E> {

    public E extractAnswerGet (String url, RestTemplate restTemplate) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.getForEntity(url, SystemReply.class);
        return extractFromResponse(response);
    }

    public <V> E extractAnswerPost (String url, V objPost,RestTemplate restTemplate) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.postForEntity(url, objPost, SystemReply.class);
        return extractFromResponse(response);
    }

    public E extractAnswerPut (String url,RestTemplate restTemplate) throws ServerAnswerException {
        ResponseEntity<SystemReply> response =
                restTemplate.exchange(url, HttpMethod.PUT, null, SystemReply.class);
        return extractFromResponse(response);
    }

    private E extractFromResponse (ResponseEntity<SystemReply> response) throws ServerAnswerException {
        SystemReply systemReply = response.getBody();
        assert systemReply != null;
        try {
            if(!SignatureVerifier.isValidReply(systemReply))
                throw new NoMajorityAnswerException();
            return convertMostFrequentAnswer(systemReply.getReply()).getResultOrThrow();
        } catch (Exception e) {
            throw new ServerAnswerException(e.getMessage());
        }
    }

    private InvokerWrapper<E> convertMostFrequentAnswer(byte[] answer) {
        String ansStr = new String(answer);
        TypeToken<InvokerWrapper<E>> collectionType = new TypeToken<InvokerWrapper<E>>() {};
        InvokerWrapper<E> l = new Gson().fromJson(ansStr, collectionType.getType());
        return l;
    }
}
