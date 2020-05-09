package pt.unl.fct.csd.Replication;

import java.io.IOException;

public interface Invoker {

    String doStuff() throws IOException, ClassNotFoundException;
}
