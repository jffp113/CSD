package pt.unl.fct.csd.cliente.Cliente.Services;

import pt.unl.fct.csd.cliente.Cliente.Model.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.*;

public class ReplyProcessor {

    private final Map<byte[], Integer> answersFrequency;

    public ReplyProcessor(List<byte[]> answers) {
        this.answersFrequency = new HashMap<>(answers.size());
        answers.forEach(answer -> answersFrequency.merge(answer, 1, Integer::sum));
    }

    public String getMostFrequentAnswer() throws Exception {
        byte[] mostFrequentAns = answersFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(NoMajorityAnswerException::new).getKey();
        return convertMostFrequentAnswer(mostFrequentAns).getResultOrThrow();
    }

    private InvokerWrapper convertMostFrequentAnswer(byte[] answer) {
        try {
            return tryToConvertMostFrequentAnswer(answer);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    private InvokerWrapper tryToConvertMostFrequentAnswer(byte[] answer)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(answer);
             ObjectInput objIn = new ObjectInputStream(byteIn)) {
            return (InvokerWrapper) objIn.readObject();
        }
    }

}
