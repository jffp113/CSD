package pt.unl.fct.csd.Model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="valueFrom", nullable = false)
    private String from;

    @Column(name="valueTo", nullable = false)
    private String to;

    @Column(name="amount", nullable = false)
    private Long amount;


    public static Transaction createToWithAmount(String to,Long amount){
       Transaction tmp = new Transaction();
       tmp.setTo(to);
       tmp.setAmount(amount);
       return tmp;
    }

    public static Transaction createFromToWithAmount(String from, String to,Long amount){
        Transaction tmp = createToWithAmount(to,amount);
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
}
