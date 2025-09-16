package io.nitishc.grievance_portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private final RedisTemplate<String, String> redisTemplate;
    @Autowired
    public RedisConfig( RedisTemplate<String, String> redisTemplate){
        this.redisTemplate=redisTemplate;
    }

    @Bean
    RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,String> redisTemp= new RedisTemplate<>();
        redisTemp.setConnectionFactory(factory);
        redisTemp.setKeySerializer(new StringRedisSerializer());
        redisTemp.setValueSerializer(new StringRedisSerializer());
        return  redisTemp;
    }

    public void setValue(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key){
       return redisTemplate.opsForValue().get(key);
    }
}
