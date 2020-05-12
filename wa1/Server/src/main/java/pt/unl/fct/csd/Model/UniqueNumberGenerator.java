package pt.unl.fct.csd.Model;

public class UniqueNumberGenerator {

    private static Long next = 0L;


    public static synchronized Long getUniqueNumber(){
        return next++;
    }

}
