package com.techchallenge.fastfood.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
@AllArgsConstructor
public class RedisConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        if (connectionFactory instanceof LettuceConnectionFactory lettuceFactory) {
            log.info("Conectando ao Redis em host={}, porta={}, database={}",
                    lettuceFactory.getHostName(),
                    lettuceFactory.getPort(),
                    lettuceFactory.getDatabase());
        } else {
            log.warn("RedisConnectionFactory não é uma instância de LettuceConnectionFactory: {}", connectionFactory.getClass().getName());
        }

        template.afterPropertiesSet();
        return template;
    }
}
