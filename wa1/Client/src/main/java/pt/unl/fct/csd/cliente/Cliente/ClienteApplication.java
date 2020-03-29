package pt.unl.fct.csd.cliente.Cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClienteApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClienteApplication.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
		//SpringApplication.run(ClienteApplication.class, args);
	}

}
