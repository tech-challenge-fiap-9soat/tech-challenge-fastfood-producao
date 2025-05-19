package com.techchallenge.fastfood.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisConfigTest {
    @Test
    @DisplayName("Deve criar RedisTemplate com os serializadores configurados")
    void deveCriarRedisTemplateComSerializadores() {
        // cenário
        ObjectMapper objectMapper = new ObjectMapper();
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);
        RedisConfig redisConfig = new RedisConfig(objectMapper);

        // ação
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(connectionFactory);

        // verificação
        assertNotNull(redisTemplate);
        assertEquals(StringRedisSerializer.class, redisTemplate.getKeySerializer().getClass());
        assertEquals(StringRedisSerializer.class, redisTemplate.getHashKeySerializer().getClass());
        assertTrue(redisTemplate.getValueSerializer().getClass().getSimpleName().contains("GenericJackson2JsonRedisSerializer"));
        assertSame(connectionFactory, redisTemplate.getConnectionFactory());
    }
}