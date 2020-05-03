package pt.unl.fct.csd.SmartContract;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class WebMVCConf implements WebMvcConfigurer {


    @Bean
    public AuthInterceptor customInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor());
    }

}
