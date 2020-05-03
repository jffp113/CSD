package pt.unl.fct.csd.SmartContract;

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
		logger.info("Token " + token);

		AuthSmartContract contract = smartContractController.getSmartContract(token);


		String[] uri = request.getRequestURI()
				.substring(1).split("/");

		logger.info(uri[0]);

		if(!contract.canDoOperation(token, uri[0]))
			throw new NotAutorizedException();

		return true;
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
