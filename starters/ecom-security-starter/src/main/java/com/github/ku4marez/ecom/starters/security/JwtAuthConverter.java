package com.github.ku4marez.ecom.starters.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final JwtSecurityProperties props;

    public JwtAuthConverter(JwtSecurityProperties props) {
        this.props = props;
    }

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        Collection<?> roles = (Collection<?>) jwt.getClaims()
            .getOrDefault(props.getRolesClaim(), List.of());
        var authorities = roles.stream()
            .map(Object::toString)
            .map(r -> props.getRolePrefix() + r)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
