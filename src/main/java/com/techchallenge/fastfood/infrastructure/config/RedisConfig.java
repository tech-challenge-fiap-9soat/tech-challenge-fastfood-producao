package com.techchallenge.fastfood.infrastructure.config;

import com.techchallenge.fastfood.infrastructure.dto.PedidoDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, PedidoDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, PedidoDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serializador de chaves como String
        template.setKeySerializer(new StringRedisSerializer());

        // Serializador de valores usando Jackson (JSON)
        Jackson2JsonRedisSerializer<PedidoDTO> serializer = new Jackson2JsonRedisSerializer<>(PedidoDTO.class);
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
