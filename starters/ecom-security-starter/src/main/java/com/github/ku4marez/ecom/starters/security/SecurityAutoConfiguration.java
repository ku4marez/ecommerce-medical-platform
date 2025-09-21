package com.github.ku4marez.ecom.starters.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

@AutoConfiguration
@EnableConfigurationProperties(JwtSecurityProperties.class)
@EnableMethodSecurity
@ConditionalOnProperty(prefix = "ecom.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    SecurityFilterChain security(HttpSecurity http, JwtDecoder decoder,
                                 org.springframework.core.convert.converter.Converter<Jwt, JwtAuthenticationToken> conv) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated())
            .oauth2ResourceServer(o -> o.jwt(j -> j.decoder(decoder).jwtAuthenticationConverter(conv)));
        return http.build();
    }

    @Bean @ConditionalOnMissingBean
    JwtDecoder jwtDecoder(JwtSecurityProperties p) {
        if (p.getJwkSetUri() != null) return NimbusJwtDecoder.withJwkSetUri(p.getJwkSetUri()).build();
        if (p.getIssuerUri() != null)  return JwtDecoders.fromIssuerLocation(p.getIssuerUri());
        if (p.getHmacSecret() != null) {
            var key = new SecretKeySpec(p.getHmacSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            return NimbusJwtDecoder.withSecretKey(key).build();
        }
        throw new IllegalStateException("Configure ecom.security.jwt.issuer-uri (or jwk-set-uri / hmac-secret)");
    }

    @Bean @ConditionalOnMissingBean
    org.springframework.core.convert.converter.Converter<Jwt, JwtAuthenticationToken> jwtAuthConverter(JwtSecurityProperties p) {
        return jwt -> {
            Collection<?> roles = (Collection<?>) jwt.getClaims().getOrDefault(p.getRolesClaim(), java.util.List.of());
            var authorities = roles.stream()
                .map(Object::toString)
                .map(r -> p.getRolePrefix() + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    /** Auditor from SecurityContext.getName() */
    @Bean @ConditionalOnMissingBean
    org.springframework.data.domain.AuditorAware<String> auditorAware() {
        return () -> java.util.Optional.ofNullable(
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
        ).map(a -> a.getName());
    }
}
