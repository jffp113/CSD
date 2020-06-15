package pt.unl.fct.csd.cliente.Cliente.Model;

import java.io.Serializable;

public class InvokerWrapper {

    private final String result;
    private final String exception;

    private InvokerWrapper(String exception, String result){
        this.exception = exception;
        this.result = result;
    }

    public static InvokerWrapper newInvokeWrapperException(String exception){
        return new InvokerWrapper(exception,null);
    }

    public static InvokerWrapper newInvokeWrapperResult(String result){
        return new InvokerWrapper(null,result);
    }

    public String getResultOrThrow() throws Exception {
        if(exception != null)
           throw new RuntimeException(exception);

        return result;
    }
}
