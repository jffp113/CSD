package pt.unl.fct.csd.cliente.Cliente.Model;


import com.google.gson.annotations.SerializedName;

public class Transaction {

    public Transaction(){}

    public Transaction(String toUser,Long amount){
        this.to = toUser;
        this.amount = amount;
    }

    public Transaction(String fromUser, String toUser,Long amount){
       this(toUser,amount);
       this.from = fromUser;
    }

    @SerializedName(value = "id", alternate = {"Id"})
    private Long id;

    @SerializedName(value = "from", alternate = {"From"})
    private String from;

    @SerializedName(value = "to", alternate = {"To"})
    private String to;

    @SerializedName(value = "amount", alternate = {"Amount"})
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
