package pt.unl.fct.csd.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pt.unl.fct.csd.Model.SystemReply;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

public interface ServerController {
    String BASE_URL = "";
    String ORDERED_URL = "/ordered";
    String UNORDERED_URL = "/unordered";

    @PostMapping(value = ORDERED_URL,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    SystemReply orderedOperation(@RequestBody byte[] val);

    @PostMapping(value = UNORDERED_URL,
            produces = APPLICATION_OCTET_STREAM_VALUE)
    SystemReply unorderedOperation(@RequestBody byte[] val);
}
