package pt.unl.fct.csd.cliente.Cliente.Services;

import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.unl.fct.csd.cliente.Cliente.exceptions.ServerAnswerException;

public class HandleServerAnswer<E> {

    E processServerAnswer (ResponseEntity<String> response, Class<E> klass) throws ServerAnswerException {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED){
            throw new ServerAnswerException("User FORBIDDEN");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            JSONObject obj = new JSONObject(response.getBody());
            String message = (String) obj.get("message");
            throw new ServerAnswerException(message);
        }
        return new Gson().fromJson(response.getBody(), klass);
    }
}
