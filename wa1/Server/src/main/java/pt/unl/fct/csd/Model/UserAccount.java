package pt.unl.fct.csd.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("UserAccount")
@Data
public class UserAccount implements Serializable {

    @Id
    @Indexed
    private String id;

    private Long money;

    public UserAccount(){

    }

    public UserAccount(String id, Long money){
        this.id = id;
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public void addMoney(Long money){
        this.money += money;
    }
}
