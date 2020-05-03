package pt.unl.fct.csd.cliente.Cliente.Model;


import java.io.Serializable;

public interface AuthSmartContract extends Serializable {

    boolean canDoOperation(String matchToken, String path);

}
