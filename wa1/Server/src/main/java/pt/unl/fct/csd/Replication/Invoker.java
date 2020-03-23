package pt.unl.fct.csd.Replication;

import java.io.IOException;

public interface Invoker<T> {

    T doStuff() throws IOException, ClassNotFoundException;
}
