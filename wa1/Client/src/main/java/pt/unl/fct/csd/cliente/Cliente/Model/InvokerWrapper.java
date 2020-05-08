package pt.unl.fct.csd.cliente.Cliente.Model;

import java.io.Serializable;

public class InvokerWrapper<E> {

    private E result;
    private Exception exception;

    private InvokerWrapper(Exception exception){
        this.exception = exception;
        this.result = null;
    }

    private InvokerWrapper(E result){
        this.exception = null;
        this.result = result;
    }

    /*public static <E extends Serializable> InvokerWrapper<E> catchInvocation(Invoker<E> invoker){
        try{
            return new InvokerWrapper<>(invoker.doStuff());
        } catch (Exception e){
            e.printStackTrace();
            return new InvokerWrapper<>(e);
        }
    }*/

    public E getResultOrThrow() throws Exception {
        if(exception != null)
           throw exception;

        return result;
    }
}
