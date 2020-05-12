package pt.unl.fct.csd.Replication;

import bftsmart.tom.AsynchServiceProxy;
import bftsmart.tom.ServiceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@PropertySource("classpath:application.properties")
@EnableRedisRepositories
@Configuration
 public class AppConfig {

    @Value("${replica.id}")
    private int ID;

    @Value("${redis.host}")
    private String redis;

     @Bean
     public ServiceProxy serviceProxy() {
         return new ServiceProxy(ID);
     }

     @Bean
     public AsynchServiceProxy AsynchServiceProxy() {
        return new AsynchServiceProxy(ID);
    }


    //DATABASE REDIS
    @Bean
    RedisConnectionFactory connectionFactory() {
         LettuceConnectionFactory r = new LettuceConnectionFactory();
         r.setHostName(redis);
        return r;
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        return template;
    }

}
