package com.currencyfair.tradeprocessor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping( "/**")
            .allowedOrigins("*")
            .allowedMethods("PUT", "DELETE", "GET", "POST")
            .allowCredentials(false).maxAge(3600);
    }
}
