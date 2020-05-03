package pt.unl.fct.csd.Replication;

import bftsmart.tom.ServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import pt.unl.fct.csd.SmartContract.AuthInterceptor;

import java.util.ArrayList;
import java.util.List;

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