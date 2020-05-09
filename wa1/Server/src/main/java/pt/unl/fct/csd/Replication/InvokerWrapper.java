package pt.unl.fct.csd.Replication;

import pt.unl.fct.csd.Exceptions.InternalServerError;

import java.io.Serializable;

public class InvokerWrapper implements Serializable{

    private String result;
    private RuntimeException exception;

    private InvokerWrapper(RuntimeException exception){
        this.exception = exception;
        this.result = null;
    }

    private InvokerWrapper(String result){
        this.exception = null;
        this.result = result;
    }

    public static InvokerWrapper catchInvocation(Invoker invoker){
        try{
            return new InvokerWrapper(invoker.doStuff());
        } catch (RuntimeException e){
            e.printStackTrace();
            return new InvokerWrapper(e);
        } catch (Exception e){
            e.printStackTrace();
            return new InvokerWrapper(new InternalServerError());
        }
    }

    public String getResultOrThrow() {
        if(exception != null)
           throw exception;

        return result;
    }
}
