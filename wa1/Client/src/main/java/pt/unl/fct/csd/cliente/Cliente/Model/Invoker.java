package pt.unl.fct.csd.cliente.Cliente.Model;

import java.io.IOException;

public interface Invoker<T> {

    T doStuff() throws IOException, ClassNotFoundException;
}
