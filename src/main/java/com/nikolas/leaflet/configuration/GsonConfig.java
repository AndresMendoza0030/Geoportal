package com.nikolas.leaflet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@Configuration
public class GsonConfig {
    @Bean
    public HttpMessageConverter<?> gsonHttpMessageConverter() {
        return new GsonHttpMessageConverter();
    }
}
