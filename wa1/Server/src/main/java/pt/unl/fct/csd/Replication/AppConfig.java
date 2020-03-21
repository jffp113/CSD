package pt.unl.fct.csd.Replication;

import bftsmart.tom.ServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Controller.WalletControllerImp;

@PropertySource("classpath:application.properties")
@Configuration
 public class AppConfig {

    @Value("${replica.id}")
    private int ID;

     @Bean
     public ServiceProxy serviceProxy() {
         return new ServiceProxy(ID);
     }

 }