package pt.unl.fct.csd.Model;

import pt.unl.fct.csd.Replication.AsyncReply;

import java.io.Serializable;
import java.util.List;

public class ReplyChain<E> implements Serializable {
    private final List<AsyncReply<E>> replies;

    public ReplyChain(List<AsyncReply<E>> replies){
        this.replies = replies;
    }

    public List<AsyncReply<E>> getReplies() {
        return replies;
    }
}
