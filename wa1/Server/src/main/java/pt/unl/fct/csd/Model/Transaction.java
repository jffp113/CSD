package pt.unl.fct.csd.Model;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Transaction")
public class Transaction implements Serializable {

    @Id
    private Long id;

    private String from;

    private String to;

    private Long amount;

    public static Transaction createToWithAmount(String to,Long amount){

       Transaction tmp = new Transaction();
       tmp.setTo(to);
       tmp.setAmount(amount);
       tmp.setId(UniqueNumberGenerator.getUniqueNumber());
       return tmp;
    }

    public static Transaction createFromToWithAmount(String from, String to,Long amount){
        Transaction tmp = createToWithAmount(to,amount);
        tmp.setId(UniqueNumberGenerator.getUniqueNumber());
        tmp.setFrom(from);

        return tmp;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
