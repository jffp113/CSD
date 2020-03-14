package pt.unl.fct.csd.cliente.Cliente.Services;


public interface Client {

    void createMoney(String toUser, Long amount);

    void transferMoney(String fromUser, String toUser, Long amount);

    Long currentAmount(String userID);
}
