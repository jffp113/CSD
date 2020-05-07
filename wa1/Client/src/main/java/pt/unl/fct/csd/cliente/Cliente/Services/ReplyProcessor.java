package pt.unl.fct.csd.cliente.Cliente.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplyProcessor {

    private final Map<byte[], Integer> answersFrequency;

    public ReplyProcessor(List<byte[]> answers) {
        this.answersFrequency = new HashMap<>(answers.size());
        for (byte[] answer : answers) {
            Integer answerFreq = answersFrequency.get(answer);
            if (answerFreq == null)
                answersFrequency.put(answer, 1);
            else
                answersFrequency.put(answer, answerFreq + 1);
        }

    }

}
