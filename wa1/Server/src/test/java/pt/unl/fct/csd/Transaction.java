package pt.unl.fct.csd;

public class Transaction {
    private String to;
    private Long amount;
    private String from;

    public Transaction(String toUser, Long amount) {
        this.to = toUser;
        this.amount = amount;
    }

    public Transaction(String fromUser, String toUser, Long amount) {
        this(toUser, amount);
        this.from = fromUser;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
