package com.techchallenge.fastfood.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Configuration
public class JacksonConfig {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Suporte para LocalDateTime (entrada e saída)
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer(formatter));

        javaTimeModule.addSerializer(LocalDateTime.class,
                new com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer(formatter));

        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
