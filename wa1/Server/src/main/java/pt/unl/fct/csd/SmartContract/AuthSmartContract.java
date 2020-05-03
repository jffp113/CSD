package pt.unl.fct.csd.SmartContract;


import pt.unl.fct.csd.Replication.Path;

import java.io.Serializable;

public interface AuthSmartContract extends Serializable {

    boolean canDoOperation(String matchToken, String path);

}
