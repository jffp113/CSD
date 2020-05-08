package pt.unl.fct.csd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import pt.unl.fct.csd.Replication.Server;

@SpringBootApplication
public class CsdProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsdProjectApplication.class, args);
	}

}
