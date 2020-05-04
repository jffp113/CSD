package pt.unl.fct.csd.SmartContract;

import org.python.core.PyBoolean;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pt.unl.fct.csd.Controller.SmartContractController;
import pt.unl.fct.csd.Exceptions.NotAutorizedException;
import pt.unl.fct.csd.Replication.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;


public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthInterceptor.class);

	@Qualifier("ImpSmart")
	@Autowired
	private SmartContractController smartContractController;

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		String token =  request.getHeader("token");

		if(token == null){
			throw new NotAutorizedException();
		}


		byte[] contract = smartContractController.getSmartContract(token);

		String[] uri = request.getRequestURI()
				.substring(1).split("/");

		logger.info("Token " + token + "to Path:" + uri[0]);

		if(uri[0].equals("error"))
			return true;

		if(!executeSmartContract(contract,uri[0]))
			throw new NotAutorizedException();

		return true;
	}

	private boolean executeSmartContract(byte[] smartContractBytes , String uri){

		String smartContract = new String(smartContractBytes);

		logger.info(smartContract);

		PythonInterpreter pi = new PythonInterpreter();
		pi.exec(smartContract);
		pi.exec("result = auth(\""+uri+"\")");
		PyBoolean result = (PyBoolean)pi.get("result");

		return result.getBooleanValue();
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
