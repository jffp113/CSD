package pt.unl.fct.csd.cliente.Cliente.Components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pt.unl.fct.csd.cliente.Cliente.Services.Client;

@ShellComponent
public class WalletRelatedCommandsImpl {
    //TODO improvement include a Terminal class with colors

    private Client client;

    @Autowired
    public WalletRelatedCommandsImpl(Client client) {
        this.client = client;
    }

    @ShellMethod("Create money to a specified user")
    public String createMoney( @ShellOption() String toUser, @ShellOption() Long amount)
    {
        client.createMoney(toUser,amount);
        //TODO model exceptions
        return "Money was created with success";
    }

    @ShellMethod("Transfer money from a provided user to other provided user")
    public String transferMoney(@ShellOption() String fromUser, @ShellOption() String toUser, @ShellOption() Long amount)
    {
        //TODO model exceptions
        client.transferMoney(fromUser, toUser, amount);
        return "Money was transfer";
    }

    @ShellMethod("See the provided user money")
    public String currentAmount(
            @ShellOption() String id)
    {
        //TODO model exceptions
        return "The user has " + client.currentAmount(id);
    }

}
