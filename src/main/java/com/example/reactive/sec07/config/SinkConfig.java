package com.example.reactive.sec07.config;

import com.example.reactive.sec07.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<ProductDto> sinks(){
        return Sinks.many().replay().limit(2);
    }

}
