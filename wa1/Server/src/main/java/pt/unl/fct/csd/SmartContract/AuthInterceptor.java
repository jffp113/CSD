package pt.unl.fct.csd.SmartContract;

import org.python.core.PyBoolean;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pt.unl.fct.csd.Controller.SmartContractController;
import pt.unl.fct.csd.Exceptions.NotAutorizedException;
import pt.unl.fct.csd.Replication.Path;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@PropertySource("classpath:application.properties")
public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthInterceptor.class);

	@Qualifier("ImpSmart")
	@Autowired
	private SmartContractController smartContractController;

	@Value("${smartcontract}")
	private boolean smartcontractOn;

	@Override
	public boolean preHandle(HttpServletRequest request,
							 HttpServletResponse response, Object handler) throws Exception {
		if(!smartcontractOn){
			return true;
		}

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

	private boolean executeSmartContract(byte[] smartContractBytes , String uri) throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("python");

		String smartContract = new String(smartContractBytes);

		logger.info(smartContract);

		engine.eval(smartContract);
		engine.eval("result = auth(\""+uri+"\")");
		Boolean n = (Boolean)engine.get("result");

		return n;
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
