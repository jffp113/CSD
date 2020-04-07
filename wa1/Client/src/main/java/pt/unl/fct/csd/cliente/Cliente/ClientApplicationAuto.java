package pt.unl.fct.csd.cliente.Cliente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pt.unl.fct.csd.cliente.Cliente.Anotation.AUTO;
import pt.unl.fct.csd.cliente.Cliente.Anotation.NONAUTO;
import pt.unl.fct.csd.cliente.Cliente.Components.AuctionRelatedCommandsImpl;


@SpringBootApplication
@AUTO
@ComponentScan(basePackages = {"pt.unl.fct.csd.cliente.Cliente"}, excludeFilters={
		@ComponentScan.Filter(type= FilterType.ANNOTATION,
				classes= NONAUTO.class)})
public class ClientApplicationAuto {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClientApplicationAuto.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}
}
