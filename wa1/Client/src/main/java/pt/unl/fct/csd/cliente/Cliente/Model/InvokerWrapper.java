package pt.unl.fct.csd.cliente.Cliente.Model;

import java.io.Serializable;

public class InvokerWrapper {

    private String result;
    private Exception exception;

    private InvokerWrapper(Exception exception){
        this.exception = exception;
        this.result = null;
    }

    private InvokerWrapper(String result){
        this.exception = null;
        this.result = result;
    }

    private InvokerWrapper() {}

    public String getResult() {
        return result;
    }

    public void setResult(String resultJson) {
        this.result = resultJson;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String getResultOrThrow() throws Exception {
        if(exception != null)
           throw exception;

        return result;
    }
}
