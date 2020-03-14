package pt.unl.fct.csd.cliente.Cliente.Model;


public class UserAccount {

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
