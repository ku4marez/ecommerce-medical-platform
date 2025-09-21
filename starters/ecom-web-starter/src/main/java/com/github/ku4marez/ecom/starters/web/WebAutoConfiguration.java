package com.github.ku4marez.ecom.starters.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;


@AutoConfiguration
@EnableConfigurationProperties(WebProps.class)
@ConditionalOnProperty(prefix="ecom.web.cors", name="enabled", havingValue="true", matchIfMissing = true)
public class WebAutoConfiguration {

    @Bean @ConditionalOnMissingBean
    CorsConfigurationSource corsConfigurationSource(WebProps p) {
        var cfg = new org.springframework.web.cors.CorsConfiguration();
        cfg.setAllowedOrigins(p.getAllowedOrigins());
        cfg.setAllowedMethods(p.getAllowedMethods());
        cfg.setAllowedHeaders(p.getAllowedHeaders());
        cfg.setAllowCredentials(p.isAllowCredentials());
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
