package pt.unl.fct.csd.cliente.Cliente.Services;

import Replication.InvokerWrapper;
import pt.unl.fct.csd.cliente.Cliente.exceptions.NoMajorityAnswerException;

import java.io.*;
import java.util.*;

public class ReplyProcessor<E extends Serializable> {

    private final Map<byte[], Integer> answersFrequency;

    public ReplyProcessor(List<byte[]> answers) {
        this.answersFrequency = new HashMap<>(answers.size());
        answers.forEach(answer -> answersFrequency.merge(answer, 1, Integer::sum));
    }

    public E getMostFrequentAnswer() throws Exception {
        byte[] mostFrequentAns = answersFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(NoMajorityAnswerException::new).getKey();
        return convertMostFrequentAnswer(mostFrequentAns).getResultOrThrow();
    }

    private InvokerWrapper<E> convertMostFrequentAnswer(byte[] answer) {
        try {
            return tryToConvertMostFrequentAnswer(answer);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    private InvokerWrapper<E> tryToConvertMostFrequentAnswer(byte[] answer)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(answer);
             ObjectInput objIn = new ObjectInputStream(byteIn)) {
            return (InvokerWrapper<E>) objIn.readObject();
        }
    }

}
